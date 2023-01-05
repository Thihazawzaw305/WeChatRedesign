package com.padcmyanmar.thiha.wechatredesign.data.models

import com.padcmyanmar.thiha.wechatredesign.network.AuthManager
import com.padcmyanmar.thiha.wechatredesign.network.FirebaseAuthManager

object AuthenticationModelImpl : AuthenticationModel{

 override var  mAuthManager: AuthManager = FirebaseAuthManager


    override fun login(
        phoneNumber: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
      mAuthManager.login(phoneNumber,password,onSuccess,onFailure)
    }

    override fun singUp(
        phoneNumber: String,
        password: String,
        userName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuthManager.singUp(phoneNumber, password, userName, onSuccess, onFailure)
    }


}