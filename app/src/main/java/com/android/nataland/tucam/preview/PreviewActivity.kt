package com.android.nataland.tucam.preview

import android.content.res.ColorStateList
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_preview.camera_view_frames_preview

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                showDiscardChangesDialog()
                true
            }
            R.id.menu_save -> {
                // todo: save image
                Toast.makeText(this, "Save pressed", Toast.LENGTH_SHORT).show()
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
        val frameId = intent.getIntExtra(FRAME_ID_TAG, 0)
        val isLensFacingFront = intent.getBooleanExtra(IS_LENS_FACING_FRONT_TAG, false)
        val canChooseFrames = intent.getBooleanExtra(CAN_CHOOSE_FRAMES_TAG, false)

        if (imageUriInString == null || frameId == 0) {
            throw Exception("image uri and frame id should never be null")
        }

        if (isLensFacingFront) {
            // todo: flip image
        }

        if (canChooseFrames) {
            setUpFramesAndFilters()
        } else {
            setUpFiltersOnly()
        }

        val imageUri = imageUriInString.toUri()
        selected_frame_view.setImageResource(frameId)
        effectsPreviewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        effectsPreviewAdapter = EffectsPreviewAdapter()
        image_preview.setImage(imageUri)
        camera_view_frames_preview.layoutManager = effectsPreviewManager
        camera_view_frames_preview.adapter = effectsPreviewAdapter
        camera_view_frames_preview.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))
        effectsPreviewAdapter.effectSelectedLiveData.observeForever {
            image_preview.filter = GPUImageFilterUtils.createFilterForType(this, GPUImageFilterUtils.defaultFilters[it].filterType)
        }
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
        const val FRAME_ID_TAG = "FRAME_ID_TAG"
        const val IS_LENS_FACING_FRONT_TAG = "IS_LENS_FACING_FRONT_TAG"
        const val CAN_CHOOSE_FRAMES_TAG = "CAN_CHOOSE_FRAMES_TAG"
    }
}
