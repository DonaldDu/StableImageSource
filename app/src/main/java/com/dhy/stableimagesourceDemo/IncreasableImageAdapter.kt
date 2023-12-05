package com.dhy.stableimagesourceDemo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhy.stableimagesource.ImageData
import com.dhy.stableimagesource.ImageHolder
import com.dhy.stableimagesource.PickUtil
import com.dhy.stableimagesourceDemo.databinding.UploadImageItemBinding

class UploadImageHolder(private val binding: UploadImageItemBinding, pickUtil: PickUtil) : RecyclerView.ViewHolder(binding.root) {
    private val imageHolder = ImageHolder<ImageData>(binding.image, binding.ivDelete, pickUtil.takePhotoOnClickListener)
    fun update(data: ImageData, position: Int) {
        imageHolder.init(data)
        binding.imageLabel.text = if (position == 0) "车头45°照" else "其它"
    }
}

class IncreasableImageAdapter(
    context: Context,
    private val maxCount: Int,
    private val datas: MutableList<ImageData>,
    private val pickUtil: PickUtil
) : RecyclerView.Adapter<UploadImageHolder>() {
    private val inflate = LayoutInflater.from(context)

    init {
        if (datas.isEmpty()) datas.add(ImageData())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageHolder {
        val binding = UploadImageItemBinding.inflate(inflate)
        return UploadImageHolder(binding, pickUtil)
    }

    override fun onBindViewHolder(holder: UploadImageHolder, position: Int) {
        holder.update(datas[position], position)
    }

    override fun getItemCount(): Int {
        val d = datas.lastOrNull { it.hasImage }
        val realCount = if (d != null) datas.indexOf(d) + 1 else 0
        return if (realCount == maxCount) maxCount
        else {
            if (datas.last().hasImage) datas.add(ImageData())
            realCount + 1
        }
    }
}