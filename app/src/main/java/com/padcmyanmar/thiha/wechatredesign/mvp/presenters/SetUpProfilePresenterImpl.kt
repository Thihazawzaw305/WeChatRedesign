package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.auth.User
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.SetUpProfileView

class SetUpProfilePresenterImpl : ViewModel(), SetUpProfilePresenter {
    var mModel = WeChatRedesignModelImpl
    var mView: SetUpProfileView? = null
    var mData: UserVO? = null

    override fun initView(view: SetUpProfileView) {
        mView = view
    }

    override fun onTapPicture() {
        mView?.pickImageFromGallery()
    }

    override fun uploadPicture(bitmap: Bitmap?) {
        if (bitmap != null) {
            mData?.let { mModel.upLoadProfile(bitmap, it) }
            mView?.navigateToMomentScreen()
        }
    }

    override fun onUiReady(owner: LifecycleOwner) {
        mModel.getUser(onSuccess = {
            mData = it
        }, onFailure = {
            mData = UserVO()
        })
    }
}