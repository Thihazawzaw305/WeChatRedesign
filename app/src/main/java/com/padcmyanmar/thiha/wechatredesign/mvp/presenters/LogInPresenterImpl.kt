package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.data.models.AuthenticationModel
import com.padcmyanmar.thiha.wechatredesign.data.models.AuthenticationModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.LogInView
import com.padcmyanmar.thiha.wechatredesign.network.CloudFirestoreFirebaseApiImpl
import com.padcmyanmar.thiha.wechatredesign.network.FirebaseApi

class LogInPresenterImpl : ViewModel(), LogInPresenter {
    private val mAuthenticationModel: AuthenticationModel = AuthenticationModelImpl
    private var mView: LogInView? = null
    private val mFirebaseApi: FirebaseApi = CloudFirestoreFirebaseApiImpl
    private lateinit var mUser: UserVO


    override fun initView(view: LogInView) {
        mView = view
    }


    override fun onTapLogIn(phoneNumber: String, password: String) {

        mAuthenticationModel.login(phoneNumber, password, onSuccess = {

            mFirebaseApi.getUserInfo(onSuccess = {
                mUser = it

            }, onFailure = {

            })
            mView?.navigateToMomentScreen()

        }, onFailure = {
            mView?.showError(it)
        })
    }


    override fun onTapBack() {
        mView?.navigateToWelcomeScreen()
    }


    override fun onUiReady(owner: LifecycleOwner) {

    }
}