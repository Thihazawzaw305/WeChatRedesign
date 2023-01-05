package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.data.models.AuthenticationModelImpl.mAuthManager
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.MeView
import kotlinx.coroutines.delay
import kotlin.concurrent.thread

class MePresenterImpl: ViewModel(), MePresenter {
    private val mModel = WeChatRedesignModelImpl
    private var meView : MeView ?= null
    private var mChosenUserForFileUpload: UserVO? = null
    var mName =""
    var mPhoneNumber =""
    var mDateOfBirth =""
     var mGender =""
    var mProfileImage =""
    var mId = ""
    var mPassword = ""
    override fun initView(view: MeView) {
       meView = view
    }

    override fun onPhotoTaken(bitmap: Bitmap) {
        mChosenUserForFileUpload?.let {
            mModel.upLoadProfile(bitmap,it)
        }

    }

//    override fun onTapUpload(user: UserVO) {
//        mChosenUserForFileUpload = user
//        meView?.openGallery()
//    }

    override fun onTapEdit() {
      meView?.showEditDialog(name = mName, phone = mPhoneNumber, dob = mDateOfBirth, gender = mGender)
    }

    override fun onTapQr() {
     meView?.showQrDialog(mId)
    }

    override fun onUiReady(owner: LifecycleOwner) {

        mModel.getUser(onSuccess = { it ->
            mId = it.id.toString()
            mName = it.name.toString()
            mPhoneNumber = it.phoneNumber.toString()
            mGender = it.gender.toString()
            mDateOfBirth = it.dateOfBirth.toString()
            mProfileImage = it.profileUrl.toString()
            meView?.bindUserData(mName,mPhoneNumber,mGender,mDateOfBirth,mProfileImage)
            mModel.getBookMarkMoments(mId, onSuccess = {
                meView?.bindMoments(it)
            }, onFailure = {

                meView?.showError(it)
            })

        }, onFailure = {
            mId = ""
            mName = ""
            mPhoneNumber = ""
            mGender = ""
            mDateOfBirth = ""
            mProfileImage = ""
            meView?.showError(it)
        })

    }

    override fun onTapLike(
        momentMillis: String,
        likeCounts: Int,
        isLike: Boolean,
        onSuccess: () -> Unit
    ) {
        mModel.giveLike(
            like = isLike,
            id = mId,
            likeCounts = likeCounts,
            momentMillis = momentMillis,
            onSuccess = {
                Log.d("reaction", "reaction success")
                onSuccess()
            },
            onFailure = {
               meView?.showError(it)
            })
    }

    override fun onTapBookmark(momentMillis: String, isBookmark: Boolean,onSuccess: () -> Unit) {
        mModel.bookMarkMoment(
            BookMark = isBookmark,
            momentMillis = momentMillis,
            id = mId,
            onSuccess = {
                Log.d("bookmark", "bookmark success")
                onSuccess()
            },
            onFailure = {
                meView?.showError(it)
            }

        )

    }

    override fun onTapLikePeople(momentMillis: String, onSuccess: () -> Unit) {

    }

    override fun onSaveProfile(name: String, phone: String, dob: String, gender: String) {
        if (phone != mPhoneNumber) {
            mAuthManager.updateUser(
                phone,
                mPassword,
                onSuccess = {
                    mModel.updateUser(
                        id = mId,
                        name = name,
                        phone = phone,
                        password = mPassword,
                        dob = dob,
                        gender = gender,
                        profileImage = mProfileImage,
                        onSuccess = {

                        },
                        onFailure = {
                            meView?.showError(it)
                        }
                    )
                },
                onFailure = {
                    meView?.showError(it)

                },
            )
        } else {
            mModel.updateUser(
                id = mId,
                name = name,
                phone = phone,
                password = mPassword,
                dob = dob,
                gender = gender,
                profileImage = mProfileImage,
                onSuccess = {

                },
                onFailure = {
                    meView?.showError(it)
                }
            )
        }
    }
}