package com.padcmyanmar.thiha.wechatredesign.activities
import EXTRA_URI
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.padcmyanmar.thiha.wechatredesign.R







val CAMERA_REQUEST_CODE = 1
class CameraActivity : AppCompatActivity() {
    var cam_uri: Uri? = null

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, CameraActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if(isCameraPermissionGiven()){
            pickCamera()
        }else{
            requestCameraPermission()
        }

    }

    private fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun isCameraPermissionGiven(): Boolean {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            QrScannerActivity.PERMISSION_REQUEST_CODE_CAMERA
        )
    }
    private fun IntArray.isPermissionGranted(): Boolean {
        return this.isNotEmpty() && this[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            QrScannerActivity.PERMISSION_REQUEST_CODE_CAMERA -> {
                if (grantResults.isPermissionGranted())
                    pickCamera()
                else
                    requestCameraPermission()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == CAMERA_REQUEST_CODE){
//            val bitmap = data?.extras?.get("data") as Bitmap
//            Log.d("bitmap",bitmap.toString())
//            data.putExtra(EXTRA_BITMAP, data.extras?.get("data").toString())
//            setResult(RESULT_OK, data)
//            finish()
//        }else{
//            Log.d("bitmap",resultCode.toString())
//
//        }
//    }

    private fun pickCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        cam_uri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri)

        startCamera.launch(cameraIntent) // VERY NEW WAY
    }


    private var startCamera: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                // There are no request codes
//                 mImageView.setImageURI(cam_uri)
                Log.d("camera","success")

                val intent = Intent()
                intent.putExtra(EXTRA_URI,cam_uri.toString())
                setResult(RESULT_OK, intent)
                finish()
            }else{
                finish()
            }
        })



}