package com.android.nataland.tucam.preview

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.android.nataland.tucam.R
import com.android.nataland.tucam.camera.FramesPreviewAdapter
import com.android.nataland.tucam.utils.FrameUtils
import com.android.nataland.tucam.utils.subscribe
import com.android.nataland.tucam.utils.subscribeToEvent
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PreviewFragment : Fragment() {
    private lateinit var previewView: PreviewView
    private val viewModel: PreviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val imageUriInString = arguments?.getString(IMAGE_URI_TAG)
        val frameIndex = arguments?.getInt(FRAME_INDEX_TAG, -1) ?: -1
        val isLensFacingFront = arguments?.getBoolean(IS_LENS_FACING_FRONT_TAG, false) ?: false
        val canChooseFrames = arguments?.getBoolean(CAN_CHOOSE_FRAMES_TAG, false) ?: false

        if (!::previewView.isInitialized) {
            val framesAdapter = FramesPreviewAdapter().apply {
                frameSelectedLiveData.observeForever {
                    viewModel.handleViewAction(PreviewViewAction.FrameSelected(it))
                }
            }
            val effectsAdapter = EffectsPreviewAdapter().apply {
                effectSelectedLiveData.observeForever {
                    viewModel.handleViewAction(PreviewViewAction.EffectSelected(it))
                }
            }

            if (imageUriInString == null) {
                throw Exception("image uri and frame id should never be null")
            }
            previewView = PreviewView.inflate(
                canSelectFrames = canChooseFrames,
                isLensFacingFront = isLensFacingFront,
                imageUri = imageUriInString.toUri(),
                layoutInflater = inflater,
                framesAdapter = framesAdapter,
                effectsAdapter = effectsAdapter,
                viewActionHandler = viewModel::handleViewAction
            )
        }
        viewModel.viewState.subscribe(this) { viewState ->
            viewState?.let { previewView.render(it) }
        }
        viewModel.handleViewAction(PreviewViewAction.Init(frameIndex))
        return previewView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.action.subscribeToEvent(this) { action ->
            handleAction(action)
            true
        }
    }

    private fun handleAction(action: PreviewAction) {
        when (action) {
            PreviewAction.NavigateUp -> requireActivity().showDiscardChangesDialog()
            is PreviewAction.SaveImage -> saveImageWithFilter(action.bitmap)
        }
    }

    private fun saveImageWithFilter(bitmapWithFilterApplied: Bitmap) {
        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.saved_fragment, arguments)
        val canvas = Canvas(bitmapWithFilterApplied)
        val frame = BitmapFactory.decodeResource(resources, FrameUtils.presetFrames[viewModel.viewState.value?.frameIndex ?: 0])
        canvas.drawBitmap(frame, null, Rect(0, 0, bitmapWithFilterApplied.width, bitmapWithFilterApplied.height), Paint())
        saveImage(bitmapWithFilterApplied, requireContext(), resources.getString(R.string.app_name))
    }

    /**
     * https://stackoverflow.com/a/57265702/9453623
     * @param folderName can be your app's name
     */
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
        }
    }

    private fun contentValues(): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val IMAGE_URI_TAG = "IMAGE_URI_TAG"
        const val FRAME_INDEX_TAG = "FRAME_INDEX_TAG"
        const val IS_LENS_FACING_FRONT_TAG = "IS_LENS_FACING_FRONT_TAG"
        const val CAN_CHOOSE_FRAMES_TAG = "CAN_CHOOSE_FRAMES_TAG"
    }
}
