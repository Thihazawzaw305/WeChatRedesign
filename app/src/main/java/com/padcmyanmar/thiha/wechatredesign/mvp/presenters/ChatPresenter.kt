package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.delegates.ChatDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.ChatView

interface ChatPresenter :BasePresenter,ChatDelegate{
    fun initView(view: ChatView)
}