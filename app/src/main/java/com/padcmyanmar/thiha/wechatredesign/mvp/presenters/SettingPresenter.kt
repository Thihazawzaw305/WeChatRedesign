package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.mvp.views.SettingView

interface SettingPresenter:BasePresenter {
    fun initView(view : SettingView)
    fun onTapLogout()
}