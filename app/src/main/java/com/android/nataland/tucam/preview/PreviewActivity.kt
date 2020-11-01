package com.android.nataland.tucam.preview

import android.content.ContentValues
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.nataland.tucam.R
import com.android.nataland.tucam.camera.FramesPreviewAdapter
import com.android.nataland.tucam.utils.FrameUtils
import kotlinx.android.synthetic.main.activity_preview.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Users should already have a photo taken or chosen at this point. This activity allows them to:
 * - Select a filter from preset filters
 * - Select a frame (if they don't have one already)
 * - Save the image with filter and frame applied
 */
class PreviewActivity : AppCompatActivity() {

    private lateinit var effectsPreviewManager: LinearLayoutManager
    private lateinit var effectsPreviewAdapter: EffectsPreviewAdapter
    private lateinit var framesPreviewManager: LinearLayoutManager
    private lateinit var framesPreviewAdapter: FramesPreviewAdapter
    private var frameIndex: Int = 0

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                showDiscardChangesDialog()
                true
            }
            R.id.menu_save -> {
                Toast.makeText(this, "Save pressed", Toast.LENGTH_SHORT).show()
                saveImageWithFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        showDiscardChangesDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_preview, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setSupportActionBar(preview_toolbar)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.black)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val imageUriInString = intent.getStringExtra(IMAGE_URI_TAG)
        frameIndex = intent.getIntExtra(FRAME_INDEX_TAG, -1)
        val isLensFacingFront = intent.getBooleanExtra(IS_LENS_FACING_FRONT_TAG, false)
        val canChooseFrames = intent.getBooleanExtra(CAN_CHOOSE_FRAMES_TAG, false)

        if (imageUriInString == null) {
            throw Exception("image uri and frame id should never be null")
        }

        if (canChooseFrames) {
            setUpFramesAndFilters()
        } else {
            setUpFiltersOnly()
        }

        selected_frame_view.setImageResource(FrameUtils.presetFrames[frameIndex])
        effectsPreviewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        effectsPreviewAdapter = EffectsPreviewAdapter()

        val imageUri = imageUriInString.toUri()
        if (isLensFacingFront) {
            gpu_image_preview.setImage(getTransformedBitmap(imageUri))
        } else {
            gpu_image_preview.setImage(imageUri)
        }

        camera_view_frames_preview.layoutManager = effectsPreviewManager
        camera_view_frames_preview.adapter = effectsPreviewAdapter
        camera_view_frames_preview.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))
        effectsPreviewAdapter.effectSelectedLiveData.observeForever {
            gpu_image_preview.filter = GPUImageFilterUtils.createFilterForType(this, GPUImageFilterUtils.defaultFilters[it].filterType)
        }
    }

    private fun saveImageWithFilter() {
        val bitmapWithFilterApplied = gpu_image_preview.gpuImage.bitmapWithFilterApplied
        val canvas = Canvas(bitmapWithFilterApplied)
        val frame = BitmapFactory.decodeResource(resources, FrameUtils.presetFrames[frameIndex])
        canvas.drawBitmap(frame, null, Rect(0, 0, bitmapWithFilterApplied.width, bitmapWithFilterApplied.height), Paint())
        saveImage(bitmapWithFilterApplied, this, resources.getString(R.string.app_name))
        // todo: Unfreeze up after image saved
    }

    /**
     * https://stackoverflow.com/a/57265702/9453623
     * @param folderName can be your app's name
     */
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)
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

    private fun getTransformedBitmap(imageUri: Uri): Bitmap {
        val matrix = Matrix()
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri))
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }
        matrix.postScale(-1f, 1f, bitmap.height.toFloat() / 2, bitmap.width.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun setUpFiltersOnly() {
        select_filter_button.isVisible = false
        select_frame_button.isVisible = false
        select_filter_button.isVisible = false
        select_frame_button.isVisible = false
        chooseEffects()
    }

    private fun setUpFramesAndFilters() {
        framesPreviewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        framesPreviewAdapter = FramesPreviewAdapter()
        framesPreviewAdapter.frameSelectedLiveData.observeForever {
            selected_frame_view.setImageResource(FrameUtils.presetFrames[it])
        }
        frames_preview.layoutManager = framesPreviewManager
        frames_preview.adapter = framesPreviewAdapter
        select_filter_button.isVisible = true
        select_frame_button.isVisible = true
        chooseFrames()
        select_filter_button.setOnClickListener {
            chooseEffects()
        }
        select_frame_button.setOnClickListener {
            chooseFrames()
        }
    }

    private fun chooseFrames() {
        select_frame_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_primary))
        select_filter_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.white))
        frames_preview.isVisible = true
        camera_view_frames_preview.isInvisible = true
    }

    private fun chooseEffects() {
        select_filter_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_primary))
        select_frame_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.white))
        frames_preview.isInvisible = true
        camera_view_frames_preview.isVisible = true
    }

    private fun showDiscardChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.discard_changes))
            .setMessage(getString(R.string.discard_changes_message))
            .setCancelable(true)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.discard)) { _, _ -> finish() }
            .show()
    }

    companion object {
        const val IMAGE_URI_TAG = "IMAGE_URI_TAG"
        const val FRAME_INDEX_TAG = "FRAME_INDEX_TAG"
        const val IS_LENS_FACING_FRONT_TAG = "IS_LENS_FACING_FRONT_TAG"
        const val CAN_CHOOSE_FRAMES_TAG = "CAN_CHOOSE_FRAMES_TAG"
    }
}
