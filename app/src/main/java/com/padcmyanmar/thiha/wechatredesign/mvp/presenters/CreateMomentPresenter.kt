package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.delegates.FileDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.CreateMomentView

interface CreateMomentPresenter:BasePresenter, FileDelegate {
    fun initView(view: CreateMomentView)
    fun onTapBack()
    fun onTapPickFile()
    fun uploadMoment(text: String,fileList: List<FileVO>)
}