package com.padcmyanmar.thiha.wechatredesign.data.models

import com.padcmyanmar.thiha.wechatredesign.network.AuthManager

interface AuthenticationModel {
    var mAuthManager : AuthManager
    fun login(phoneNumber : String, password : String, onSuccess :() -> Unit, onFailure: (String) -> Unit)
    fun singUp(phoneNumber:String, password: String, userName: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
  }