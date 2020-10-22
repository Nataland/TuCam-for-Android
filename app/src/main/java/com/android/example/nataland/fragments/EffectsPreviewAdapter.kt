package com.android.example.nataland.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.example.nataland.R
import kotlinx.android.synthetic.main.view_effects_preview.view.*

class EffectsPreviewAdapter(
    private val effects: List<CameraFragment.Effect>
) : RecyclerView.Adapter<EffectsPreviewAdapter.PreviewViewHolder>() {

    private val _frameSelectedLiveData = MutableLiveData<Int>()
    val frameSelectedLiveData: LiveData<Int> = _frameSelectedLiveData

    class PreviewViewHolder(val previewView: View) : RecyclerView.ViewHolder(previewView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val previewView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_effects_preview, parent, false)

        return PreviewViewHolder(previewView)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        when (val effect = effects[position]) {
            is CameraFragment.Effect.Filter -> {
                // todo
            }
            is CameraFragment.Effect.Frame -> {
                holder.previewView.frame.setImageResource(effect.frameId)
                holder.previewView.setOnClickListener { _frameSelectedLiveData.postValue(position) } // change frame selected using live data
            }
        }
    }

    override fun getItemCount() = effects.size
}