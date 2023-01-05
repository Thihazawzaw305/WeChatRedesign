package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.graphics.Bitmap
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import com.padcmyanmar.thiha.wechatredesign.delegates.EditProfileDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.MomentDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.MeView

interface MePresenter :BasePresenter, MomentDelegate,EditProfileDelegate{
    fun initView(view : MeView)
    fun onPhotoTaken(bitmap: Bitmap)
  //  fun onTapUpload(user : UserVO)
    fun onTapEdit()
    fun onTapQr()
}