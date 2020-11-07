package com.android.nataland.tucam.camera

import android.net.Uri
import com.android.nataland.tucam.utils.ViewState

data class CameraViewState(
    val isLensFacingFront: Boolean = true,
    val flashState : FlashState = FlashState.OFF,
    val timerState: TimerState = TimerState.OFF,
    val isGridVisible: Boolean = false,
    val frameIndex: Int = 0,
    val isSwitchButtonEnabled: Boolean = false,
    val shouldSimulateCapturePressed: Boolean = false,
    val capturedImage: Uri? = null,
    val countDown: Int? = null
) : ViewState

enum class FlashState {
    OFF, ON, AUTO
}

enum class TimerState {
    OFF, THREE_SECONDS, TEN_SECONDS
}
