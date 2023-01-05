package com.padcmyanmar.thiha.wechatredesign.network

interface AuthManager {
    fun login(phoneNumber : String, password : String, onSuccess :() -> Unit, onFailure: (String) -> Unit)
    fun singUp(phoneNumber:String, password: String, userName: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun updateUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

}