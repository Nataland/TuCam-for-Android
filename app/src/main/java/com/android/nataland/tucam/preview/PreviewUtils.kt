package com.android.nataland.tucam.preview

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.android.nataland.tucam.R

fun Activity.showDiscardChangesDialog() {
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.discard_changes))
        .setMessage(getString(R.string.discard_changes_message))
        .setCancelable(true)
        .setNegativeButton(getString(R.string.no)) { _, _ -> }
        .setPositiveButton(getString(R.string.discard)) { _, _ -> finish() }
        .show()
}
