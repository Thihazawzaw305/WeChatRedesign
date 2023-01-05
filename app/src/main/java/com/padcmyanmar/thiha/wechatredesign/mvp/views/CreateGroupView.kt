package com.padcmyanmar.thiha.wechatredesign.mvp.views

import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO

interface CreateGroupView :BaseView{
    fun bindContacts(contactList: List<ContactVO>)
    fun pickGroupImage()
    fun createGroupSuccess()
    fun navigateBack()
}