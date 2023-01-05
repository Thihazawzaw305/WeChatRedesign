package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.graphics.Bitmap
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactSelectDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.CreateGroupView

interface CreateGroupPresenter:BasePresenter , ContactDelegate, ContactSelectDelegate {
    fun initView(view: CreateGroupView)
    fun pickGroupImage()
    fun onTapBack()
    fun createGroup(name: String, bitmap: Bitmap)
}