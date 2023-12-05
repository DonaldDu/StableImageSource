package com.dhy.stableimagesource

import android.Manifest
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.facebook.drawee.view.SimpleDraweeView
import java.io.File

interface PickImagePresenter<STATE : ImageState> {
    val state: STATE
    val takePhotoLauncher: ActivityResultLauncher<Uri>
    val pickImageLauncher: ActivityResultLauncher<PickImageOption?>
    val requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    fun takePhoto(data: ImageData) {
        data.tmp = createTempFile()
        state.uri = getFileUri(data.tmp!!)
        val ps = arrayOf(Manifest.permission.CAMERA)
        if (hasPermissions(ps)) {
            state.permissionOkThenTakePhoto = null
            takePhotoLauncher.launch(state.uri)
        } else {
            state.permissionOkThenTakePhoto = true
            requestPermissionLauncher.launch(ps)
        }
    }

    fun hasPermissions(permissions: Array<String>): Boolean
    fun pickImage(data: ImageData) {
        data.tmp = createTempFile()
        state.pickedImage = data.tmp
        val ps = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasPermissions(ps)) {
            state.permissionOkThenTakePhoto = null
            val option = if (data.hasImage) null else state.pickImageOption
            pickImageLauncher.launch(option)//pick image count:1
        } else {
            state.permissionOkThenTakePhoto = false
            requestPermissionLauncher.launch(ps)
        }
    }

    fun getFileUri(file: File): Uri

    fun createTempFile(): File {
        return File.createTempFile("tmp", ".jpg")
    }

    fun afterRequestPermission(permissionResult: Map<String, Boolean>) {
        if (permissionResult.any { !it.value }) return showRequestPermissionFailed(permissionResult)
        if (state.permissionOkThenTakePhoto == true) {
            takePhotoLauncher.launch(state.uri)
        } else if (state.permissionOkThenTakePhoto == false) {
            pickImageLauncher.launch(state.pickImageOption)
        }
    }

    /**
     * 如果用户误点，一直没权限，需要显示原因和引导
     * */
    fun showRequestPermissionFailed(permissionResult: Map<String, Boolean>)
    fun onTakePhotoBack(taken: Boolean) {
        if (taken) showImage()
    }

    /**
     * @param images null means cancel pick image
     * */
    fun onPickImageBack(images: List<File>?) {
        if (images == null) return
        val tag = if (state.replaceImage) state.viewTag else null
        val handled = state.pickImageOption?.onMultipleImagesPicked?.onEvent(tag, images)
        if (handled != true) updateSingle(images)
    }

    private fun updateSingle(images: List<File>?) {
        val f = images?.firstOrNull()
        if (f.isOK && state.pickedImage != null) {
            val v = ImageRoot.find(state.viewTag) as? SimpleDraweeView
            val data = v?.imageData
            if (data != null) {
                state.pickedImage = f
                data.tmp = f
                data.imageUpdater?.showImage()
            } else {
                f!!.copyTo(state.pickedImage!!, true)
            }
        }
    }

    private fun showImage() {
        val v = ImageRoot.find(state.viewTag) as? SimpleDraweeView
        v?.imageData?.imageUpdater?.showImage()
    }
}