package com.android.nataland.tucam.preview

import android.graphics.Bitmap

sealed class PreviewViewAction {
    object NavigateUp : PreviewViewAction()
    data class Init(val frame: Int) : PreviewViewAction()
    data class FrameSelected(val index: Int) : PreviewViewAction()
    data class EffectSelected(val index: Int) : PreviewViewAction()
    data class SaveImage(val bitmap: Bitmap) : PreviewViewAction()
}
