package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.delegates.ContactDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactSelectDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.GroupDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.ContactView

interface ContactPresenter:BasePresenter , ContactDelegate, ContactSelectDelegate, GroupDelegate {
    fun initView(view : ContactView)
    fun onTapAddContact()
    fun onTapCreateGroup()
    fun refreshGroupList()
    fun addContact(friendId: String)

}