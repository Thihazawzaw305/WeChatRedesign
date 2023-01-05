package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.mvp.views.SignUpView

interface SignUpPresenter : BasePresenter{
    fun initView(view : SignUpView)
    fun onTapSignUp( phoneNumber: String, password: String, userName: String, dateOfBirth : String, gender : String, profileUrl: String)
    fun onTapBack()

}