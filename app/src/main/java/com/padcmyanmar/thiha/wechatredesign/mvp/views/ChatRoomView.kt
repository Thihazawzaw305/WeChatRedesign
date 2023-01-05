package com.padcmyanmar.thiha.wechatredesign.mvp.views

import CONTENT
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.MessageVO

interface ChatRoomView :BaseView{
    fun onTapContent(content: CONTENT)
    fun bindMessages(ownId: String,messageList: List<MessageVO>)
    fun navigateBack()
    fun navigateToCameraScreen()
    fun onFileRemove(fileVO: FileVO)
}