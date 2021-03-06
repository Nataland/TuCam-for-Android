package com.android.nataland.tucam.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.nataland.tucam.R
import com.android.nataland.tucam.utils.GPUImageFilterUtils
import kotlinx.android.synthetic.main.view_effect_preview.view.*

class EffectsPreviewAdapter : RecyclerView.Adapter<EffectsPreviewAdapter.PreviewViewHolder>() {

    class PreviewViewHolder(val previewView: View) : RecyclerView.ViewHolder(previewView)

    private val _effectSelectedLiveData = MutableLiveData<Int>()
    val effectSelectedLiveData: LiveData<Int> = _effectSelectedLiveData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val previewView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_effect_preview, parent, false)

        return PreviewViewHolder(previewView)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.previewView.effect_preview_root.setImageResource(GPUImageFilterUtils.defaultFilters[position].drawableRes)
        holder.previewView.effect_preview_root.setOnClickListener {
            _effectSelectedLiveData.postValue(position)
        }
    }

    override fun getItemCount() = GPUImageFilterUtils.defaultFilters.size
}