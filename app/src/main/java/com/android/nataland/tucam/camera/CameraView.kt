package com.android.nataland.tucam.camera

import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.camera.core.Preview
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.nataland.tucam.R
import com.android.nataland.tucam.utils.FrameUtils
import com.android.nataland.tucam.utils.simulateClick
import kotlinx.android.synthetic.main.fragment_camera.view.*

class CameraView(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet) {
    private lateinit var framesPreviewAdapter: FramesPreviewAdapter
    private lateinit var cameraViewActionHandler: (CameraViewAction) -> Unit
    private lateinit var surfaceProvider: Preview.SurfaceProvider

    fun getSurfaceProvider() = surfaceProvider

    fun render(viewState: CameraViewState) {
        configureCameraCapture(viewState)
        viewState.capturedImage?.let {
            captured_image_view.isVisible = true
            loading_progress_bar.isVisible = true
            captured_image_view.setImageURI(it)
        } ?: run {
            captured_image_view.isVisible = false
            loading_progress_bar.isVisible = false
        }
        captured_image_view.scaleX = if (viewState.isLensFacingFront) -1f else 1f

        if (viewState.shouldSimulateCapturePressed) {
            camera_capture_button.simulateClick()
            cameraViewActionHandler.invoke(CameraViewAction.SimulateCapturePressedFinished)
        }

        camera_flash_button.setImageResource(when (viewState.flashState) {
            FlashState.OFF -> R.drawable.ic_flash_off
            FlashState.ON -> R.drawable.ic_flash_on
            FlashState.AUTO -> R.drawable.ic_flash_auto
        })

        camera_timer_button.setImageResource(when (viewState.timerState) {
            TimerState.OFF -> R.drawable.ic_timer_off
            TimerState.THREE_SECONDS -> R.drawable.ic_timer_three_seconds
            TimerState.TEN_SECONDS -> R.drawable.ic_timer_ten_seconds
        })

        camera_grid_button.setImageResource(when (viewState.isGridVisible) {
            true -> R.drawable.ic_grid_on
            false -> R.drawable.ic_grid_off
        })

        camera_switch_button.isEnabled = viewState.isSwitchButtonEnabled
        frame_overlay.setImageResource(FrameUtils.presetFrames[viewState.frameIndex])
        grid_lines_view.isVisible = viewState.isGridVisible
    }

    private fun configureCameraCapture(viewState: CameraViewState) {
        camera_capture_button.setOnClickListener {
            if (viewState.timerState == TimerState.OFF) {
                captureImage()
            } else {
                toggleControls(false)
                object : CountDownTimer(
                    if (viewState.timerState == TimerState.THREE_SECONDS) THREE_SECONDS_IN_MILLI else TEN_SECONDS_IN_MILLI,
                    ONE_SECOND_IN_MILLI
                ) {
                    override fun onFinish() {
                        captureImage()
                        toggleControls(true)
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        countdown_text_view.isVisible = true
                        countdown_text_view.text = ((millisUntilFinished / ONE_SECOND_IN_MILLI) + 1).toString()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                countdown_text_view.isVisible = false
                            },
                            800
                        )
                    }
                }.start()
            }
        }
    }

    private fun toggleControls(isEnabled: Boolean) {
        camera_flash_button.isEnabled = isEnabled
        camera_grid_button.isEnabled = isEnabled
        camera_switch_button.isEnabled = isEnabled
        camera_timer_button.isEnabled = isEnabled
        camera_capture_button.isEnabled = isEnabled
    }

    private fun captureImage() {
        cameraViewActionHandler.invoke(CameraViewAction.CameraCapturePressed)

        val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = FADE_OUT_DURATION
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    shutter_effect.alpha = 0.0f
                    shutter_effect.isVisible = false
                }

                override fun onAnimationStart(animation: Animation?) = Unit
            })
        }
        val fadeInAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = FADE_IN_DURATION
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    shutter_effect.alpha = 1.0f
                    Handler(Looper.getMainLooper()).postDelayed({
                        shutter_effect.startAnimation(fadeOutAnimation)
                    }, FADE_IN_DURATION)
                }

                override fun onAnimationStart(animation: Animation?) {
                    shutter_effect.isVisible = true
                }
            })
        }
        shutter_effect.startAnimation(fadeInAnimation)
    }

    private fun configureGalleryButton() {
        open_gallery_button.setOnClickListener {
            cameraViewActionHandler.invoke(CameraViewAction.PickFromGalleryPressed)
        }
    }

    private fun configureFrameSelectionButton() {
        frame_selection_button.setOnClickListener {
            camera_frames_preview.isVisible = !camera_frames_preview.isVisible
            val mediumIconSize = context.resources.getDimensionPixelSize(R.dimen.round_button_medium).toFloat()
            val largeIconSize = context.resources.getDimensionPixelSize(R.dimen.round_button_large).toFloat()
            val scale = if (camera_frames_preview.isVisible) mediumIconSize / largeIconSize else 1f
            camera_capture_button.animate().scaleX(scale).scaleY(scale).apply { duration = 100 }.start()
        }
    }

    private fun configureToolbar() {
        camera_grid_button.setOnClickListener { cameraViewActionHandler.invoke(CameraViewAction.GridButtonPressed) }
        camera_timer_button.setOnClickListener { cameraViewActionHandler.invoke(CameraViewAction.TimerButtonPressed) }
        camera_flash_button.setOnClickListener { cameraViewActionHandler.invoke(CameraViewAction.FlashButtonPressed) }
        camera_switch_button.setOnClickListener { cameraViewActionHandler.invoke(CameraViewAction.SwitchButtonPressed) }
    }

    companion object {
        private const val FADE_IN_DURATION: Long = 20
        private const val FADE_OUT_DURATION: Long = 250
        private const val THREE_SECONDS_IN_MILLI: Long = 3000
        private const val TEN_SECONDS_IN_MILLI: Long = 10000
        private const val ONE_SECOND_IN_MILLI: Long = 1000

        fun inflate(
            layoutInflater: LayoutInflater,
            adapter: FramesPreviewAdapter,
            viewActionHandler: (CameraViewAction) -> Unit,
        ): CameraView {
            return (layoutInflater.inflate(R.layout.fragment_camera, null, false) as CameraView).apply {
                framesPreviewAdapter = adapter
                cameraViewActionHandler = viewActionHandler
                surfaceProvider = view_finder.surfaceProvider
                camera_frames_preview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                camera_frames_preview.adapter = framesPreviewAdapter
                camera_frames_preview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
                configureToolbar()
                configureFrameSelectionButton()
                configureGalleryButton()
            }
        }
    }
}
