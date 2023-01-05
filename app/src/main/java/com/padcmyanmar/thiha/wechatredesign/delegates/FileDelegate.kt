package com.padcmyanmar.thiha.wechatredesign.delegates

import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO

interface FileDelegate {
    fun onTapDelete(fileVO : FileVO)
}