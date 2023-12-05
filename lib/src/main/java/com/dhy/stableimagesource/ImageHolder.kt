package com.dhy.stableimagesource

import android.net.Uri
import android.view.View
import androidx.annotation.DrawableRes
import com.dhy.xintent.show
import com.facebook.drawee.view.SimpleDraweeView

open class ImageHolder<T : ImageData>(val image: SimpleDraweeView, val delete: View, photoOnClickListener: View.OnClickListener) {
    var data: T? = null

    @DrawableRes
    open val failureImage: Int? = null

    init {
        image.setOnClickListener(photoOnClickListener)
        delete.setOnClickListener {
            deleteImage()
        }
    }

    open fun init(data: T) {
        this.data = data
        image.imageData = data
        data.imageUpdater = this
        ImageRoot.add(data.viewTag, image)//获取到图片后，通过TAG查找View
        showImage()
    }

    open fun deleteImage() {
        data?.apply {
            if (file.isCache(image.context)) file?.delete()
            file = null
        }
        showImage()
    }

    open fun showImage() {
        val pic = data ?: return
        val hasImage = pic.hasImage
        delete.show(hasImage)
        showImage(image, pic.getUri(image.context))
    }

    open fun showImage(image: SimpleDraweeView, uri: Uri?) {
        if (failureImage == null) {
            image.setImageURI(uri, null)
        } else {
            if (uri != null) {
                image.hierarchy.setFailureImage(failureImage!!)
                image.setImageURI(uri, null)
            } else {
                image.hierarchy.setFailureImage(null)
                image.setImageURI("")
            }
        }
    }
}
