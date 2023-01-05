package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.CreateMomentView
import kotlinx.coroutines.withContext

class CreateMomentPresenterImpl:ViewModel(), CreateMomentPresenter {
    private var mView : CreateMomentView ?=null
    private var mModel = WeChatRedesignModelImpl
    private lateinit var mId : String
    private lateinit var mName : String
    private lateinit var mProfileImage : String

    override fun initView(view: CreateMomentView) {
       mView = view
    }

    override fun onTapBack() {
        mView?.navigateBack()
    }

    override fun onTapPickFile() {
       mView?.pickFiles()
    }

    override fun uploadMoment(text: String, fileList: List<FileVO>) {
        mView?.showLoadingDialog()
        mModel.uploadMoment(text, fileList, mId, mName, mProfileImage, onSuccess = {
            mView?.dismissLoadingDialog()
            Log.d("multi_file", "moment create success")
        }, onFailure = {
            mView?.showError(it)
            mView?.dismissLoadingDialog()
        })
    }

    override fun onUiReady(owner: LifecycleOwner) {
     mModel.getUser(onSuccess = {
         mName = it.name.toString()
        mProfileImage = it.profileUrl.toString()
        mId = it.id.toString()
         mView?.onBindUserData(mName,mProfileImage)
     }, onFailure = {
         mView?.showError(it)
     }
     )
    }

    override fun onTapDelete(fileVO: FileVO) {
     mView?.onFileRemove(fileVO )
    }
}