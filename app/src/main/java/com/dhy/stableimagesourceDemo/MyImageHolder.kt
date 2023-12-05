package com.dhy.stableimagesourceDemo

import android.view.View
import com.dhy.stableimagesource.ImageData
import com.dhy.stableimagesource.ImageHolder
import com.dhy.xintent.show
import com.facebook.drawee.view.SimpleDraweeView

class MyImageHolder(image: SimpleDraweeView, delete: View, photoOnClickListener: View.OnClickListener) : ImageHolder<ImageData>(image, delete, photoOnClickListener) {
    override fun showImage() {
        val pic = data ?: return
        val hasImage = pic.hasImage
        delete.show(hasImage)
        val uri = pic.getUri(image.context)
        if (uri != null) {
            image.setImageURI(uri, null)
        } else {
            image.hierarchy.setFailureImage(null)
            image.setImageURI("")
        }
    }
}