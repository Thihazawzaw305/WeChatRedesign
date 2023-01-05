package com.padcmyanmar.thiha.wechatredesign.delegates

import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO


interface ContactDelegate {
    fun onTapContact(contactVO: ContactVO)
}