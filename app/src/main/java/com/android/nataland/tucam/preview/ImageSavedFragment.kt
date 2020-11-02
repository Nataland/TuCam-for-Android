package com.android.nataland.tucam.preview

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.android.nataland.tucam.R
import kotlinx.android.synthetic.main.fragment_image_saved.*

class ImageSavedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_image_saved, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reedit_button.setOnClickListener {
            // todo: pass in arguments and navigate
//            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigateUp()
        }
        home_button.setOnClickListener {
            requireActivity().finish()
        }
        success_checkmark_view.drawable.startVectorAnimation()
    }

    private fun Drawable.startVectorAnimation() {
        val mutatedDrawable = this.mutate()
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
            (mutatedDrawable as AnimatedVectorDrawableCompat).apply {
                start()
            }
        } else {
            (mutatedDrawable as AnimatedVectorDrawable).apply {
                start()
            }
        }
    }
}
