package com.android.nataland.tucam.preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.android.nataland.tucam.R

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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navHostFragment.navController.setGraph(R.navigation.nav_graph_preview, intent.extras)
    }

    override fun onBackPressed() {
        finish()
    }
}
