package com.dhy.stableimagesource

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog

interface PickImageUtil<STATE : ImageState> : PickImagePresenter<STATE> {
    fun onClickHolder(v: View) {
        val context = v.context
        val holder = v.imageData
        state.replaceImage = holder.hasImage
        if (holder.hasImage) {
            showDialogWithItems(context, getActions(true)) {
                when (it) {
                    0 -> showImagePreview(context, holder)
                    1 -> takePhoto(v)
                    else -> pickImage(v)
                }
            }
        } else {
            showDialogWithItems(context, getActions(false)) {
                if (it == 0) takePhoto(v)
                else pickImage(v)
            }
        }
    }

    fun getActions(hasImage: Boolean): Array<String> {
        return if (hasImage) arrayOf("查看图片", "拍照(替换该图片)", "相册选择(替换该图片)")
        else arrayOf("拍照", "相册选择")
    }

    fun showImagePreview(context: Context, data: ImageData)

    fun showDialogWithItems(context: Context, items: Array<String>, onItem: (index: Int) -> Unit) {
        AlertDialog.Builder(context)
            .setItems(items) { dialog, which ->
                dialog.dismiss()
                onItem(which)
            }.show()
    }

    fun takePhoto(v: View) {
        state.viewTag = v.tag.toString()
        takePhoto(v.imageData)
    }

    fun pickImage(v: View) {
        state.viewTag = v.tag.toString()
        pickImage(v.imageData)
    }
}





