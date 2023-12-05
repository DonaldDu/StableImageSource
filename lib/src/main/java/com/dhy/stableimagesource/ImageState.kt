package com.dhy.stableimagesource

import android.net.Uri
import android.os.Bundle
import java.io.File
import java.io.Serializable

open class ImageState : Serializable {
    var viewTag: String? = null
    var pickedImage: File? = null
    private var uriString: String? = null
    var pickImageOption: PickImageOption? = null
    var replaceImage: Boolean = false

    /**
     * Uri是Parcelable不是Serializable，所以需要转换后保存
     * */
    var uri: Uri?
        get() {
            return if (uriString != null) Uri.parse(uriString) else null
        }
        set(value) {
            uriString = value?.toString()
        }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(this)
    }

    /**
     *  take photo or pick image
     * */
    var permissionOkThenTakePhoto: Boolean? = null
}
