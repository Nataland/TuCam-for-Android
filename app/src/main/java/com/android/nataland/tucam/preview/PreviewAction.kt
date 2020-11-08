package com.android.nataland.tucam.preview

import android.graphics.Bitmap

sealed class PreviewAction {
    object NavigateUp : PreviewAction()
    data class SaveImage(val bitmap: Bitmap) : PreviewAction()
}
