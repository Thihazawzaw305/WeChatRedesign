package com.padcmyanmar.thiha.wechatredesign.mvp.views

import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO

interface CreateMomentView: BaseView {
    fun navigateBack()
    fun pickFiles()
    fun onBindUserData(name: String, profileImage: String)
    fun showLoadingDialog()
    fun dismissLoadingDialog()
    fun onFileRemove(fileVO: FileVO)

}