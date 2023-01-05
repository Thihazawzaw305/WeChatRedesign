package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.content.Context
import com.padcmyanmar.thiha.wechatredesign.mvp.views.LogInView
import com.padcmyanmar.thiha.wechatredesign.mvp.views.WelcomeView

interface LogInPresenter : BasePresenter{
    fun initView(view: LogInView)
    fun onTapLogIn(phoneNumber: String, password: String)
    fun onTapBack()
}