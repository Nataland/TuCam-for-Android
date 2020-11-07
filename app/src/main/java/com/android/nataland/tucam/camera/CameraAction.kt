package com.android.nataland.tucam.camera

sealed class CameraAction {
    object FlashButtonPressed : CameraAction()
    object SwitchButtonPressed : CameraAction()
    object CameraCapturePressed : CameraAction()
    object PickFromGalleryPressed : CameraAction()
}
