package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import androidx.lifecycle.LifecycleOwner

interface BasePresenter {
    fun onUiReady(owner : LifecycleOwner)
}