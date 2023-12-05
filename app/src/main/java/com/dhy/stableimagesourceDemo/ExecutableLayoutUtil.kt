package com.dhy.stableimagesourceDemo

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class ExecutableLayoutUtil(private val root: View) {
    private val data: WeakHashMap<ViewGroup, MutableMap<String, String>> = WeakHashMap()
    fun start() {
        data.clear()
        update(root)
    }

    private fun initParams(view: View, params: MutableMap<String, String>? = null): MutableMap<String, String>? {
        var map = params
        val contentDescription = view.contentDescription
        if (contentDescription != null) {
            var des = contentDescription.toString()
            if (des.startsWith("\${")) {
                //"${count=1}"
                des = des.substring(2, des.length - 1) //"count=1"
                val lines = des.split(";")
                for (line in lines) {
                    if (line.contains("=")) {
                        val nameValue = line.split("=")
                        if (map == null) map = mutableMapOf()
                        map[nameValue[0]] = nameValue[1]
                    }
                }
            }
        }
        if (view is ViewGroup && map != null) data[view] = map
        return map
    }

    private fun update(view: View) {
        if (view is ViewGroup) {
            initParams(view)
            val count = view.childCount
            for (i in 0 until count) {
                update(view.getChildAt(i))
            }
        } else {
            execute(view)
        }
    }

    private fun execute(v: View) {
        if (v is TextView) {
            v.formatWitName()
        }
    }

    private fun TextView.formatWitName() {
        val v = this
        val params = initParams(v, null)
        if (params != null) {
            val text = params["text"]
            if (text != null) {
                v.text = text.formatWitName(v)
            }
        }
    }

    private fun String.formatWitName(v: View?): String {
        var s = this
        if (v == root) return s
        val p = v?.parent as? ViewGroup ?: return s
        if (s.contains("{")) {
            val map = data[p]
            if (map != null) s = s.formatWitName(map)
        }
        if (s.contains("{")) {
            s = s.formatWitName(p)
        }
        return s
    }

    private fun String.formatWitName(params: Map<String, String>): String {
        var s = this
        params.forEach {
            val key = "{${it.key}}"
            if (s.contains(key)) s = s.replace(key, it.value)
        }
        return s
    }
}