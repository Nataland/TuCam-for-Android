package com.android.nataland.tucam.camera

sealed class CameraViewAction {
    object GridButtonPressed : CameraViewAction()
    object FlashButtonPressed : CameraViewAction()
    object SwitchButtonPressed : CameraViewAction()
    object TimerButtonPressed : CameraViewAction()
    object CameraCapturePressed : CameraViewAction()
    object PickFromGalleryPressed : CameraViewAction()
    object SimulateCapturePressed : CameraViewAction()
    object SimulateCapturePressedFinished : CameraViewAction()
    data class CameraInitialized(val isSwitchButtonEnabled: Boolean) : CameraViewAction()
    data class FrameSelected(val index: Int) : CameraViewAction()
}
