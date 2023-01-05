package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.MessageVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ContentDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.FileDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.ChatRoomView

interface ChatRoomPresenter:BasePresenter, ContentDelegate, FileDelegate {
    fun initView(view: ChatRoomView)
    fun onUiReadyWithId(context: Context, owner: LifecycleOwner, otherId: String, isGroup: Boolean)
    fun sentMessage(contactVO: ContactVO, fileList: List<FileVO>, message: MessageVO)
    fun onTapBack()
    fun onTapCamera()
}