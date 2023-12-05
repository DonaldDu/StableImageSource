package com.dhy.stableimagesource

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.dhy.xintent.toast

abstract class PickUtil(val activity: AppCompatActivity) : PickImageUtil<ImageState>, DefaultLifecycleObserver {
    override var state = ImageState()
    override val takePhotoLauncher = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { taken ->
        onTakePhotoBack(taken)
    }

    override val pickImageLauncher = activity.registerForActivityResult(PickImageResultContract()) {
        onPickImageBack(it)
    }

    override val requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        afterRequestPermission(it)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        state = savedInstanceState.readSerializable(state)

        activity.lifecycle.addObserver(this)
    }

    fun onSaveInstanceState(outState: Bundle) {
        state.onSaveInstanceState(outState)
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return activity.hasPermissions(permissions)
    }

    override fun showRequestPermissionFailed(permissionResult: Map<String, Boolean>) {
        val camera = permissionResult[Manifest.permission.CAMERA]
        val msg = if (camera == false) {
            "申请【相机】权限失败，请手动授权"
        } else {
            "申请【文件】权限失败，请手动授权"
        }
        activity.toast(msg)
    }

    val takePhotoOnClickListener = View.OnClickListener {
        onClickHolder(it)
    }

    private var dialog: Dialog? = null
    override fun showDialogWithItems(context: Context, items: Array<String>, onItem: (index: Int) -> Unit) {
        dialog?.dismiss()
        dialog = AlertDialog.Builder(context)
            .setItems(items) { dialog, which ->
                dialog.dismiss()
                onItem(which)
            }.show()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (dialog?.isShowing == true) dialog!!.dismiss()
    }
}