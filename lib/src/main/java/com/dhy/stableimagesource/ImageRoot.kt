package com.dhy.stableimagesource

import android.view.View
import java.lang.ref.WeakReference

/**
 * 把所有图片View注册到一个Map中配合WeakReference<View>，方便查找。
 * 不能在ViewRoot中查找，因为有些View还没来得急加到父节点中。因此所有View初始化时，要做加载检查。
 * */
object ImageRoot {
    private val data: MutableMap<String, WeakReference<View>> = mutableMapOf()

    /**
     * 重复调用不影响
     * */
    fun add(viewTag: String, v: View) {
        v.tag = viewTag
        data[viewTag] = WeakReference(v)
    }

    fun find(viewTag: String?): View? {
        if (viewTag == null) return null
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            val v = iterator.next().value.get()
            if (v == null) iterator.remove()
            else if (v.tag == viewTag) return v
        }
        return null
    }
}