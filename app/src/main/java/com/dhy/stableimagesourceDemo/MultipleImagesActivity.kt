package com.dhy.stableimagesourceDemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dhy.stableimagesource.*
import com.dhy.stableimagesourceDemo.databinding.ActivityMultipleImagesBinding
import java.io.File
import java.io.Serializable
import kotlin.math.min

class MultipleImagesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMultipleImagesBinding.inflate(layoutInflater) }
    private var data = TaskData()
    private val picUtil = MyPickUtil(this)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        data = savedInstanceState.readSerializable(data)
        picUtil.onCreate(savedInstanceState)

        val maxCount = 3
        val pickImageOption = MyPickImageOption(maxCount, data.imageDataList)
        pickImageOption.onMultipleImagesPicked = object : OnMultipleImagesPicked {
            override fun onEvent(viewTag: String?, images: List<File>): Boolean {
                if (viewTag == null) updatePicked(data.imageDataList, images, maxCount)
                else {
                    data.imageDataList.find { it.viewTag == viewTag }?.tmp = images.firstOrNull()
                }
                binding.rv.adapter?.notifyDataSetChanged()
                return true
            }
        }
        val adapter = IncreasableImageAdapter(this, maxCount, data.imageDataList, picUtil)
        binding.rv.adapter = adapter
        picUtil.state.pickImageOption = pickImageOption
    }

    /**
     * 已有的保持不变，余下的插入空位。
     * 已选中的图片，只能单独选择一张来替换。
     * */
    private fun updatePicked(olds: MutableList<ImageData>, news: List<File>, maxCount: Int) {
        val newImages = news.toMutableList()
        newImages.removeAll(olds.mapNotNull { it.file })

        val it = olds.iterator()
        while (it.hasNext()) {
            val old = it.next()
            if (!old.hasImage) {
                old.file = newImages.removeFirstOrNull()
            }
        }
        if (olds.size < maxCount && newImages.isNotEmpty()) {
            val end = min(newImages.size, maxCount - olds.size)
            olds.addAll(newImages.subList(0, end).map { ImageData().apply { file = it } })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        picUtil.onSaveInstanceState(outState)
        outState.putSerializable(data)
    }

    private class MyPickImageOption(val maxCount: Int, val carImages: List<ImageData>) : PickImageOption {
        override val selectCount: Int get() = maxCount
        override val checkedList: List<File>
            get() {
                return carImages.mapNotNull { if (it.file.isOK) it.file else null }
            }

        @Transient
        override var onMultipleImagesPicked: OnMultipleImagesPicked? = null
    }

    private class TaskData : Serializable {
        val imageDataList: MutableList<ImageData> = mutableListOf()
    }
}