package com.android.nataland.tucam.preview

import com.android.nataland.tucam.utils.ViewState

data class PreviewViewState(
    val canSelectFrames: Boolean = true,
    val effectIndex: Int = 0,
    val frameIndex: Int = 0,
    val isSelectingFrames: Boolean = true
) : ViewState
