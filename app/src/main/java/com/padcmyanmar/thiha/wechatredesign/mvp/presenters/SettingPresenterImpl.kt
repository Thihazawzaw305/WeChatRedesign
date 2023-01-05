package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.mvp.views.SettingView

class SettingPresenterImpl : ViewModel(), SettingPresenter{
    var mView : SettingView ?=null
    override fun initView(view: SettingView) {
      mView = view
    }

    override fun onTapLogout() {
     mView?.showConfrimDialog()
    }

    override fun onUiReady(owner: LifecycleOwner) {

    }



}