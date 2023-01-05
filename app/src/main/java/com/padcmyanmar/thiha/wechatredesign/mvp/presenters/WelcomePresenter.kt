package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.mvp.views.WelcomeView

interface  WelcomePresenter : BasePresenter {
    fun initView(view: WelcomeView)
    fun onTapLogin()
    fun onTapSignUp()
}