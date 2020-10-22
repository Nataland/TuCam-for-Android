package com.android.example.nataland.preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.example.nataland.R
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setSupportActionBar(preview_toolbar)

        val imageUriInString = intent.getStringExtra(IMAGE_URI_TAG)
        val frameId = intent.getIntExtra(FRAME_ID_TAG, 0)

        if (imageUriInString == null || frameId == 0) {
            throw Exception("image uri and frame id should never be null")
        }

        val imageUri = imageUriInString.toUri()
        gpu_image_view.setImage(imageUri)
        selected_frame_view.setImageResource(frameId)

        // Todo: configure recycler view with effect previews
    }

    companion object {
        const val IMAGE_URI_TAG = "IMAGE_URI_TAG"
        const val FRAME_ID_TAG = "FRAME_ID_TAG"
    }
}