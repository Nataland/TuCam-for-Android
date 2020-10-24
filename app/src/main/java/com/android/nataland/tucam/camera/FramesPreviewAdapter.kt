package com.android.nataland.tucam.camera

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.nataland.tucam.R
import com.android.nataland.tucam.utils.FrameUtils
import kotlinx.android.synthetic.main.view_frame_preview.view.*

class FramesPreviewAdapter : RecyclerView.Adapter<FramesPreviewAdapter.PreviewViewHolder>() {

    private val _frameSelectedLiveData = MutableLiveData<Int>()
    val frameSelectedLiveData: LiveData<Int> = _frameSelectedLiveData

    class PreviewViewHolder(val previewView: View) : RecyclerView.ViewHolder(previewView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val previewView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_frame_preview, parent, false)

        return PreviewViewHolder(previewView)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.previewView.frame.setImageResource(FrameUtils.presetFrames[position])
        holder.previewView.setOnClickListener { _frameSelectedLiveData.postValue(position) } // change frame selected using live data
    }

    override fun getItemCount() = FrameUtils.presetFrames.size
}
