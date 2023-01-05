package com.padcmyanmar.thiha.wechatredesign.mvp.views

import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO

interface ChatView :BaseView{
    fun bindContacts(contactList: List<ContactVO>)
    fun bindLastMessage(contactList: List<ContactVO>)
    fun navigateToChatRoomScreen(contactVO: ContactVO)
}