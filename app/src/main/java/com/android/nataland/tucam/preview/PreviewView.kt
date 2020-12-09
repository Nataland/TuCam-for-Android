package com.android.nataland.tucam.preview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.nataland.tucam.R
import com.android.nataland.tucam.camera.FramesPreviewAdapter
import com.android.nataland.tucam.utils.FrameUtils
import com.android.nataland.tucam.utils.GPUImageFilterUtils
import kotlinx.android.synthetic.main.fragment_preview.view.*

class PreviewView(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    private lateinit var framesPreviewAdapter: FramesPreviewAdapter
    private lateinit var effectsPreviewAdapter: EffectsPreviewAdapter
    private lateinit var cameraViewActionHandler: (PreviewViewAction) -> Unit

    fun render(viewState: PreviewViewState) {
        selected_frame_view.setImageResource(FrameUtils.presetFrames[viewState.frameIndex])
        gpu_image_preview.filter = GPUImageFilterUtils.createFilterForType(
            context,
            GPUImageFilterUtils.defaultFilters[viewState.effectIndex].filterType
        )
    }

    private fun chooseFrames() {
        select_frame_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_primary))
        select_filter_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.white))
        frames_preview.isVisible = true
        effects_preview.isInvisible = true
    }

    private fun chooseEffects() {
        select_filter_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_primary))
        select_frame_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.white))
        frames_preview.isInvisible = true
        effects_preview.isVisible = true
    }

    private fun getTransformedBitmap(imageUri: Uri): Bitmap {
        val matrix = Matrix()
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imageUri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }
        matrix.postScale(-1f, 1f, bitmap.height.toFloat() / 2, bitmap.width.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        fun inflate(
            canSelectFrames: Boolean,
            isLensFacingFront: Boolean,
            imageUri: Uri,
            layoutInflater: LayoutInflater,
            framesAdapter: FramesPreviewAdapter,
            effectsAdapter: EffectsPreviewAdapter,
            viewActionHandler: (PreviewViewAction) -> Unit
        ): PreviewView {
            return (layoutInflater.inflate(R.layout.fragment_preview, null, false) as PreviewView).apply {
                preview_toolbar.inflateMenu(R.menu.menu_preview)
                preview_toolbar.setNavigationOnClickListener {
                    viewActionHandler.invoke(PreviewViewAction.NavigateUp)
                }
                preview_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                preview_toolbar.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_save -> {
                            viewActionHandler.invoke(PreviewViewAction.SaveImage(gpu_image_preview.gpuImage.bitmapWithFilterApplied))
                            true
                        }
                        else -> false
                    }
                }

                if (canSelectFrames) {
                    select_filter_button.isVisible = true
                    select_frame_button.isVisible = true
                    chooseFrames()
                    select_filter_button.setOnClickListener {
                        chooseEffects()
                    }
                    select_frame_button.setOnClickListener {
                        chooseFrames()
                    }
                } else {
                    select_filter_button.isVisible = false
                    select_frame_button.isVisible = false
                    select_filter_button.isVisible = false
                    select_frame_button.isVisible = false
                    chooseEffects()
                }

                if (isLensFacingFront) {
                    gpu_image_preview.setImage(getTransformedBitmap(imageUri))
                } else {
                    gpu_image_preview.setImage(imageUri)
                }

                framesPreviewAdapter = framesAdapter
                effectsPreviewAdapter = effectsAdapter
                cameraViewActionHandler = viewActionHandler

                effects_preview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                effects_preview.adapter = effectsPreviewAdapter
                effects_preview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))

                frames_preview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                frames_preview.adapter = framesPreviewAdapter
                frames_preview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
            }
        }
    }
}
