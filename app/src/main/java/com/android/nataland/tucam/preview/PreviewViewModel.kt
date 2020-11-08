package com.android.nataland.tucam.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.nataland.tucam.utils.Event
import com.android.nataland.tucam.utils.postEvent

class PreviewViewModel : ViewModel() {
    private val _viewState = MutableLiveData<PreviewViewState>()
    val viewState: LiveData<PreviewViewState> = _viewState

    private val _action = MutableLiveData<Event<PreviewAction>>()
    val action: LiveData<Event<PreviewAction>> = _action

    fun handleViewAction(viewAction: PreviewViewAction) {
        when (viewAction) {
            PreviewViewAction.NavigateUp -> _action.postEvent(PreviewAction.NavigateUp)
            is PreviewViewAction.FrameSelected -> onFrameSelected(viewAction.index)
            is PreviewViewAction.EffectSelected -> onEffectSelected(viewAction.index)
            is PreviewViewAction.SaveImage -> _action.postEvent(PreviewAction.SaveImage(viewAction.bitmap))
            is PreviewViewAction.Init -> _viewState.postValue(PreviewViewState(frameIndex = viewAction.frame))
        }
    }

    private fun onFrameSelected(index: Int) {
        postViewState { it.copy(frameIndex = index) }
    }

    private fun onEffectSelected(index: Int) {
        postViewState { it.copy(effectIndex = index) }
    }

    private fun postViewState(viewStateHandler: (PreviewViewState) -> PreviewViewState) {
        _viewState.value?.let {
            _viewState.postValue(viewStateHandler.invoke(it))
        }
    }
}
