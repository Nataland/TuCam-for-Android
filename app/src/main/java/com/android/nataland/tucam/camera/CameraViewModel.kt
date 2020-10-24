package com.android.nataland.tucam.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.nataland.tucam.utils.Event
import com.android.nataland.tucam.utils.postEvent

class CameraViewModel : ViewModel() {
    private val _viewState = MutableLiveData<CameraViewState>().apply {
        postValue(CameraViewState())
    }
    val viewState: LiveData<CameraViewState> = _viewState

    private val _action = MutableLiveData<Event<CameraAction>>()
    val action: LiveData<Event<CameraAction>> = _action

    fun handleViewAction(viewAction: CameraViewAction) {
        when (viewAction) {
            CameraViewAction.GridButtonPressed -> onGridButtonPressed()
            CameraViewAction.FlashButtonPressed -> onFlashButtonPressed()
            CameraViewAction.SwitchButtonPressed -> onSwitchButtonPressed()
            CameraViewAction.TimerButtonPressed -> onTimerButtonPressed()
            CameraViewAction.CameraCapturePressed -> onCameraCapturePressed()
            CameraViewAction.PickFromGalleryPressed -> onPickFromGalleryPressed()
            CameraViewAction.SimulateCapturePressed -> updateSimulateCapturePressedState(true)
            CameraViewAction.SimulateCapturePressedFinished -> updateSimulateCapturePressedState(false)
            is CameraViewAction.FrameSelected -> onFrameSelected(viewAction.index)
            is CameraViewAction.CameraInitialized -> onSwitchButtonEnabledStateChanged(viewAction.isSwitchButtonEnabled)
        }
    }

    private fun onGridButtonPressed() {
        postViewState { it.copy(isGridVisible = !it.isGridVisible) }
    }

    private fun onFlashButtonPressed() {
        postViewState {
            it.copy(flashState = when (it.flashState) {
                FlashState.OFF -> FlashState.AUTO
                FlashState.ON -> FlashState.OFF
                FlashState.AUTO -> FlashState.ON
            })
        }
        _action.postEvent(CameraAction.FlashButtonPressed)
    }

    private fun onSwitchButtonPressed() {
        postViewState { it.copy(isLensFacingFront = !it.isLensFacingFront) }
        _action.postEvent(CameraAction.SwitchButtonPressed)
    }

    private fun onTimerButtonPressed() {
        postViewState {
            it.copy(timerState = when (it.timerState) {
                TimerState.OFF -> TimerState.THREE_SECONDS
                TimerState.THREE_SECONDS -> TimerState.TEN_SECONDS
                TimerState.TEN_SECONDS -> TimerState.OFF
            })
        }
        _action.postEvent(CameraAction.TimerButtonPressed)
    }

    private fun onCameraCapturePressed() {
        _action.postEvent(CameraAction.CameraCapturePressed)
    }

    private fun onFrameSelected(index: Int) {
        postViewState { it.copy(frameIndex = index) }
    }

    private fun onPickFromGalleryPressed() {
        _action.postEvent(CameraAction.PickFromGalleryPressed)
    }

    private fun onSwitchButtonEnabledStateChanged(isEnabled: Boolean) {
        postViewState { it.copy(isSwitchButtonEnabled = isEnabled) }
    }

    private fun updateSimulateCapturePressedState(shouldSimulateCapturePressed: Boolean) {
        postViewState { it.copy(shouldSimulateCapturePressed = shouldSimulateCapturePressed) }
    }

    private fun postViewState(viewStateHandler: (CameraViewState) -> CameraViewState) {
        _viewState.value?.let {
            _viewState.postValue(viewStateHandler.invoke(it))
        }
    }

    /*
    private val subscriptions = CompositeSubscription()

    init {
        subscriptions += _action.subscribeForever {}
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

     */
}
