package com.padcmyanmar.thiha.wechatredesign.data.vos

import android.graphics.Bitmap
import android.net.Uri

data class FileVO (
    val bitmap : Bitmap,
    val isMovie : Boolean = false,
    val uri : Uri,
    val realPath : String = ""
        )