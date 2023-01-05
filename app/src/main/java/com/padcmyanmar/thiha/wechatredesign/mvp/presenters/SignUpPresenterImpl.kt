package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.data.models.AuthenticationModel
import com.padcmyanmar.thiha.wechatredesign.data.models.AuthenticationModelImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.SignUpView
import com.padcmyanmar.thiha.wechatredesign.network.CloudFirestoreFirebaseApiImpl
import com.padcmyanmar.thiha.wechatredesign.network.FirebaseApi

class SignUpPresenterImpl : ViewModel(), SignUpPresenter {
    private val mAuthenticationModel: AuthenticationModel = AuthenticationModelImpl
    private val mFirebaseApi: FirebaseApi = CloudFirestoreFirebaseApiImpl
    private var mView: SignUpView? = null
    override fun initView(view: SignUpView) {
        mView = view
    }

    override fun onTapSignUp(phoneNumber: String, password: String, userName: String, dateOfBirth : String, gender : String, profileUrl: String) {
       mView?.showLoadingDialog()
        mAuthenticationModel.singUp(phoneNumber, password, userName, onSuccess = {
            mView?.dismissLoadingDialog()
            mFirebaseApi.createUser(phoneNumber, userName, dateOfBirth, gender, profileUrl)
            mView?.navigateToSetUpProfile()
        }, onFailure = {
            mView?.showError(it)
        }

        )
    }


    override fun onTapBack() {
        mView?.navigateToGetOTPScreen()
    }

    override fun onUiReady(owner: LifecycleOwner) {
    }
}