package com.dhy.stableimagesourceDemo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File


class MyFileProvider : FileProvider() {
    companion object {
        fun getFileUri(context: Context, file: File): Uri {
            return file.toUri(context)
        }
    }
}

@SuppressLint("ObsoleteSdkInt")
private fun File.toUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, "${context.packageName}.FileProvider", this)
    } else Uri.fromFile(this)
}