package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.padcmyanmar.thiha.wechatredesign.mvp.views.WelcomeView

class WelcomePresenterImpl : ViewModel(), WelcomePresenter {

    private var mWelcomeView: WelcomeView ?= null


    override fun initView(view: WelcomeView) {
       mWelcomeView = view
    }

    override fun onTapLogin() {
        mWelcomeView?.navigateToLoginScreen()
    }

    override fun onTapSignUp() {
        mWelcomeView?.navigateToSignUpScreen()
    }

    override fun onUiReady(owner: LifecycleOwner) {
//        val mFirebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
//
//      if( mFirebaseAuth.currentUser != null){
//          mWelcomeView?.navigateToHomeScreen()
  //    }
    }

}