package com.padcmyanmar.thiha.wechatredesign.mvp.views

import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO

interface MeView:BaseView {
    fun bindUserData( name: String, phoneNumber: String, gender : String, dateOfBirth : String, profileImage : String)
//    fun openGallery()
    fun bindMoments(momentList: List<MomentVO>)
    fun showEditDialog(name: String, phone: String, dob: String, gender: String)
    fun showQrDialog(qrCode: String)


}