package com.padcmyanmar.thiha.wechatredesign.activities

import EXTRA_QR
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.padcmyanmar.thiha.wechatredesign.R
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode

class QrScannerActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE_CAMERA = 3333

        fun getIntent(context: Context): Intent {
            val intent = Intent(context, QrScannerActivity::class.java)
            return intent
        }
    }

    private val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        val data = Intent()
        if(result is QRResult.QRSuccess){
            val qrCode = result as QRResult.QRSuccess
            Log.d("qr_code_raw", qrCode.content.rawValue)
            data.putExtra(EXTRA_QR, qrCode.content.rawValue)
            setResult(RESULT_OK, data)
            finish()

        }else{
            data.putExtra(EXTRA_QR, "")
            finish()
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        if(isCameraPermissionGiven()){
            scanQrCodeLauncher.launch(null)
        }else{
            requestCameraPermission()
        }

    }

//    private fun setUpQrCamera(){
//        codeScanner = CodeScanner(this, scanner_qr)
//
//        // Parameters (default values)
//        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
//        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
//        // ex. listOf(BarcodeFormat.QR_CODE)
//        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
//        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
//        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
//        codeScanner.isFlashEnabled = false // Whether to enable flash or not
//
//        // Callbacks
//        codeScanner.decodeCallback = DecodeCallback {
//
//        }
//        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
//
//        }
//
//        scanner_qr.setOnClickListener {
//            codeScanner.startPreview()
//        }
//    }

    private fun isCameraPermissionGiven(): Boolean {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE_CAMERA
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
            PERMISSION_REQUEST_CODE_CAMERA -> {
                if (grantResults.isPermissionGranted())
                    scanQrCodeLauncher.launch(null)
                else
                    requestCameraPermission()
            }
        }
    }





}
