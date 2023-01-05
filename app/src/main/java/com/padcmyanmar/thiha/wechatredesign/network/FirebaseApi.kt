package com.padcmyanmar.thiha.wechatredesign.network

import android.graphics.Bitmap
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO

interface FirebaseApi {
    fun getUserInfo(onSuccess: (user: UserVO) -> Unit, onFailure: (String) -> Unit)
    fun getMoment(
        id: String,
        onSuccess: (moment: List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun createUser(
        phoneNumber: String,
        name: String,
        dateOfBirth: String,
        gender: String,
        profileUrl: String
    )

    fun upLoadProfile(image: Bitmap, user: UserVO)
    fun updateProfile(
        profileImage: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun uploadMoment(
        description: String, fileList: List<FileVO>, id: String,
        name: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )


    fun giveLike(
        like: Boolean,
        momentMillis: String,
        id: String,
        likeCounts: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun bookMarkMoment(
        isBookMark: Boolean,
        momentMillis: String,
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getBookMarkMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getLikePeople(
        millis : String,
        onSuccess: (user:List<UserVO> ) -> Unit,
        onFailure: (String) -> Unit
    )

    fun updateUser(
        id: String,
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )


    fun addContacts(
        selfId: String,
        friendId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getContacts(
        id: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFailure: (String) -> Unit
    )

}