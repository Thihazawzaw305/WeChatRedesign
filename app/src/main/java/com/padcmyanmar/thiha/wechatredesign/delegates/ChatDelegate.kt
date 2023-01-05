package com.padcmyanmar.thiha.wechatredesign.delegates

import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO

interface ChatDelegate {
    fun onTapChat(contactVO: ContactVO)
}