package com.dhy.stableimagesource

import java.io.File
import java.io.Serializable

interface PickImageOption : Serializable {
    val selectCount: Int get() = 1
    val checkedList: List<File>? get() = null
    var onMultipleImagesPicked: OnMultipleImagesPicked?
        get() = null
        set(_) {}
}

interface OnMultipleImagesPicked {
    /**
     * @param viewTag 如果不为空，则指定更新该项
     * */
    fun onEvent(viewTag: String?, images: List<File>): Boolean
}