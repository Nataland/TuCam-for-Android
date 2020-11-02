package com.android.nataland.tucam.preview

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.nataland.tucam.R
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.fragment_preview.*

/**
 * Users should already have a photo taken or chosen at this point. This activity allows them to:
 * - Select a filter from preset filters
 * - Select a frame (if they don't have one already)
 * - Save the image with filter and frame applied
 */
class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.black)
        setSupportActionBar(preview_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onBackPressed() {
        showDiscardChangesDialog()
    }
}
