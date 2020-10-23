package com.android.example.nataland.preview

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.example.nataland.R
import jp.co.cyberagent.android.gpuimage.GPUImageView
import kotlinx.android.synthetic.main.view_effects_preview.view.*

class EffectsPreviewAdapter(
    private val processedImages: List<Drawable>
) : RecyclerView.Adapter<EffectsPreviewAdapter.PreviewViewHolder>() {

    class PreviewViewHolder(val previewView: View) : RecyclerView.ViewHolder(previewView)

    private val _effectSelectedLiveData = MutableLiveData<Int>()
    val effectSelectedLiveData: LiveData<Int> = _effectSelectedLiveData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val previewView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_effects_preview, parent, false)

        return PreviewViewHolder(previewView)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
//        holder.previewView.effects_preview_root.removeAllViews()
//        holder.previewView.effects_preview_root.addView(processedImages[position])
        holder.previewView.effects_preview_root.setImageDrawable(processedImages[position])
        holder.previewView.effects_preview_root.setOnClickListener {
            _effectSelectedLiveData.postValue(position)
        }
    }

    override fun getItemCount() = processedImages.size
}