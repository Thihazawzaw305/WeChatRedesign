package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.delegates.MomentDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.MomentView

interface MomentPresenter:BasePresenter,MomentDelegate {
    fun initView(view : MomentView)
    fun onTapCreateMoment()
}