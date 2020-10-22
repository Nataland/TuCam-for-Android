package com.android.example.nataland.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.example.nataland.R

class EffectsPreviewAdapter(
    private val effects: List<Int>
) : RecyclerView.Adapter<EffectsPreviewAdapter.PreviewViewHolder>() {

    private val _effectSelectedLiveData = MutableLiveData<Int>()
    val effectSelectedLiveData: LiveData<Int> = _effectSelectedLiveData

    class PreviewViewHolder(val previewView: View) : RecyclerView.ViewHolder(previewView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val previewView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_effects_preview, parent, false)

        return PreviewViewHolder(previewView)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
//        holder.previewView.effects_preview_root.setImageResource(effects[position]) // set filter
        holder.previewView.setOnClickListener { _effectSelectedLiveData.postValue(position) } // change frame selected using live data
    }

    override fun getItemCount() = effects.size
}