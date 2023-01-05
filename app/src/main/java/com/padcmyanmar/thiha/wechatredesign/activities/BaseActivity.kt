package com.padcmyanmar.thiha.wechatredesign.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    var SELECTED_INTENT_TYPE = INTENT_TYPE_IMAGE

    companion object {
        const val PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 1111
        const val INTENT_TYPE_IMAGE = "image/*"
        const val INTENT_TYPE_VIDEO = "video/*"
        const val INTENT_TYPE_FILE = "*/*"
        const val INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY = 2222
    }

    protected fun selectImageFromGallery(type: String) {
        SELECTED_INTENT_TYPE = type
        if (isOSLaterThanAndroidM())
            if (isReadStoragePermissionGiven()) pickImageFromGallery() else requestReadExternalStoragePermission()
        else
            pickImageFromGallery()
    }


    protected fun showSnackBar(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isPermissionGranted())
                    pickImageFromGallery()
                else
                    requestReadExternalStoragePermission()
            }
        }
    }

    private fun IntArray.isPermissionGranted(): Boolean {
        return this.isNotEmpty() && this[0] == PackageManager.PERMISSION_GRANTED
    }

    private fun isOSLaterThanAndroidM(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.M
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isReadStoragePermissionGiven(): Boolean {
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestReadExternalStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE
        )
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = SELECTED_INTENT_TYPE
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY)
    }
}
