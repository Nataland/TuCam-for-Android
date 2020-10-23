package com.android.example.nataland.preview

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.nataland.R
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import kotlinx.android.synthetic.main.activity_preview.*


class PreviewActivity : AppCompatActivity() {

    private lateinit var effectsPreviewManager: LinearLayoutManager
    private lateinit var effectsPreviewAdapter: EffectsPreviewAdapter
    private val filters = listOf(GPUImageFilter(), GPUImageSepiaToneFilter(), GPUImageMonochromeFilter())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setSupportActionBar(preview_toolbar)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.black)

        val imageUriInString = intent.getStringExtra(IMAGE_URI_TAG)
        val frameId = intent.getIntExtra(FRAME_ID_TAG, 0)

        if (imageUriInString == null || frameId == 0) {
            throw Exception("image uri and frame id should never be null")
        }

        val imageUri = imageUriInString.toUri()
        selected_frame_view.setImageResource(frameId)
        val processedSampleImages = listOf<Drawable>()

        // Configure toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Todo: configure recycler view with effect previews

        effectsPreviewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        effectsPreviewAdapter = EffectsPreviewAdapter(processedSampleImages)
        image_preview.setImageURI(imageUri)
        effects_preview.layoutManager = effectsPreviewManager
        effects_preview.adapter = effectsPreviewAdapter
        effectsPreviewAdapter.effectSelectedLiveData.observeForever {
//            image_preview.setImageBitmap(images[it].gpuImage.bitmapWithFilterApplied)
        }
    }

    companion object {
        const val IMAGE_URI_TAG = "IMAGE_URI_TAG"
        const val FRAME_ID_TAG = "FRAME_ID_TAG"
    }
}