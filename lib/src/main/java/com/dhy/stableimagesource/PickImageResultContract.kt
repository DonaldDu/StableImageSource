package com.dhy.stableimagesource

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.BasicWrapper
import java.io.File

class PickImageResultContract : ActivityResultContract<PickImageOption?, List<File>?>() {
    override fun createIntent(context: Context, input: PickImageOption?): Intent {
        val option = input ?: object : PickImageOption {}
        val album = Album.album(context as Activity)
            .columnCount(2)
            .selectCount(option.selectCount)
            .camera(false)
        if (!option.checkedList.isNullOrEmpty()) {
            val checkedList = ArrayList(option.checkedList!!.map { it.absolutePath })
            album.checkedList(checkedList)
        }
        val m = BasicWrapper::class.java.getDeclaredMethod("getIntent")
        m.isAccessible = true
        return m.invoke(album) as Intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<File>? {
        if (resultCode == Activity.RESULT_OK) {
            return Album.parseResult(intent).map { File(it) }
        }
        return null
    }
}