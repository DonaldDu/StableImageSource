package com.dhy.stableimagesource

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.Serializable

fun Context.hasPermissions(permissions: Array<String>): Boolean {
    return permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

internal fun <V : View> View.get(index: Int): V {
    @Suppress("UNCHECKED_CAST")
    return (this as ViewGroup).getChildAt(index) as V
}

val File?.isOK: Boolean get() = this?.exists() == true && length() > 0

fun <T : Serializable> Bundle.readSerializable(clazz: Class<T>): T? {
    val key = clazz.name
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        clazz.cast(getSerializable(key))
    }
}

fun Bundle.putSerializable(data: Serializable?) {
    if (data != null) putSerializable(data.javaClass.name, data)
}

inline fun <reified T : Serializable> Bundle?.readSerializable(): T? {
    return this?.readSerializable(T::class.java)
}

inline fun <reified T : Serializable> Bundle?.readSerializable(default: T): T {
    return this?.readSerializable(T::class.java) ?: default
}