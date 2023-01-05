package com.padcmyanmar.thiha.wechatredesign.delegates

import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO


interface ContactSelectDelegate {
    fun onSelectContact(isSelect: Boolean, contactVO: ContactVO)
}