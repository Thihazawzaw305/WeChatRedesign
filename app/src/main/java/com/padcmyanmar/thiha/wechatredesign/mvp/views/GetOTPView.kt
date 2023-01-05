package com.padcmyanmar.thiha.wechatredesign.mvp.views

interface GetOTPView: BaseView {
    fun navigateToSignUpScreen(phoneNumber : String)
    fun navigateToWelcomeScreen()
}