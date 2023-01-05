package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.mvp.views.GetOTPView

interface GetOTPPresenter: BasePresenter {
    fun initView(view : GetOTPView)
    fun onTapVerify(phoneNumber : String)
    fun onTapGetOTP()
    fun onTapBack()
}