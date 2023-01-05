package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.mvp.views.GetOTPView

class GetOTPPresenterImpl : ViewModel(), GetOTPPresenter {


    private var mView: GetOTPView? = null

    override fun initView(view: GetOTPView) {
        mView = view
    }

    override fun onTapVerify(phoneNumber: String) {
        mView?.navigateToSignUpScreen(phoneNumber)
    }

    override fun onTapGetOTP() {

    }

    override fun onTapBack() {
       mView?.navigateToWelcomeScreen()
    }

    override fun onUiReady(owner: LifecycleOwner) {

    }
}