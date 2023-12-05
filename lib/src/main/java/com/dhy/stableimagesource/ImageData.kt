package com.dhy.stableimagesource

import android.content.Context
import android.net.Uri
import android.view.View
import java.io.File
import java.io.Serializable
import java.util.*

/**
 * 给每个需要拍照的View绑定一个能自动缓存的Data，
 * 其中包括两个文件路径，一个临时和一个确认后文件。
 * 显示图片时，如果临时文件有效，则替换确认文件，并置空临时文件。
 * */
open class ImageData : Serializable {
    /**
     * 设置给VIew的tag，用来查找view。
     * 反序列化时，不会调用默认构造函数，而是直接生成实例。
     * */
    val viewTag: String = UUID.randomUUID().toString()

    /**
     * 拍照临时文件，文件需要在cacheDir下，以便自动清理。
     * */
    var tmp: File? = null

    /**
     * 拍照返回或相册选择文件
     * */
    var file: File? = null

    @Transient
    var imageUpdater: ImageHolder<*>? = null

    /**
     * 获取显示文件URI，不能手动选择用哪个文件。
     * 确认选择时，自动清理无用文件。
     * */
    open fun getUri(context: Context): Uri? {
        if (tmp.isOK) {
            if (file.isOK && file.isCache(context)) file!!.delete()
            file = tmp
            tmp = null
        }
        return if (file.isOK) Uri.fromFile(file) else null
    }

    open val hasImage: Boolean
        get() {
            return file.isOK || tmp.isOK
        }


    fun reset() {
        tmp = null
        file = null
    }
}

fun File?.isCache(context: Context): Boolean {
    val f = this ?: return false
    return f.absolutePath.startsWith(context.cacheDir.absolutePath)
}

var View.imageData: ImageData
    get() {
        val h = getTag(R.id.IMAGE_DATA) as? ImageData
        return h ?: ImageData().apply { imageData = this }
    }
    set(value) {
        setTag(R.id.IMAGE_DATA, value)
    }