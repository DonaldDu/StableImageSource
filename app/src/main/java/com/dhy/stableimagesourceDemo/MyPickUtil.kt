package com.dhy.stableimagesourceDemo

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.dhy.stableimagesource.ImageData
import com.dhy.stableimagesource.PickUtil
import com.dhy.xintent.toast
import java.io.File

class MyPickUtil(activity: AppCompatActivity) : PickUtil(activity) {
    override fun getFileUri(file: File): Uri {
        return MyFileProvider.getFileUri(activity, file)
    }

    override fun showImagePreview(context: Context, data: ImageData) {
        context.toast("showImagePreview")
    }
}