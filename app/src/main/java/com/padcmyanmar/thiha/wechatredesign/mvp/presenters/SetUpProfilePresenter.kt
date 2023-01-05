package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.graphics.Bitmap
import com.padcmyanmar.thiha.wechatredesign.mvp.views.SetUpProfileView

interface SetUpProfilePresenter : BasePresenter {
    fun initView(view : SetUpProfileView)
    fun onTapPicture()
    fun uploadPicture(bitmap: Bitmap?)
}