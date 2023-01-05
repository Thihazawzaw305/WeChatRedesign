package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.graphics.Bitmap
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.CreateGroupView

class CreateGroupPresenterImpl: ViewModel(), CreateGroupPresenter {
    val mWeChatModel =WeChatRedesignModelImpl
    var mId: String = ""
    var mName: String = ""
    var mPhotoUrl: String = ""
    private var selectedContactList: ArrayList<ContactVO> = arrayListOf()
    var mView : CreateGroupView ?=null
    override fun initView(view: CreateGroupView) {
      mView = view
    }

    override fun pickGroupImage() {
        mView?.pickGroupImage()
    }

    override fun onTapBack() {
        mView?.navigateBack()
    }

    override fun createGroup(name: String, bitmap: Bitmap) {
        val selfContactVO = ContactVO(
            id = mId,
            name = mName,
            photoUrl = mPhotoUrl
        )

        selectedContactList.add(selfContactVO)
        mWeChatModel.createGroup(
            name = name,
            bitmap = bitmap,
            contactList = selectedContactList,
            onSuccess = {
                mView?.createGroupSuccess()
            },
            onFail = {
                mView?.showError(it)
            }
        )
    }

    override fun onUiReady(owner: LifecycleOwner) {
        mWeChatModel.getUser(onSuccess = {
            mId = it.id.toString()
            mName = it.name.toString()
            mPhotoUrl = it.profileUrl.toString()
            getContacts(mId)

        }, onFailure = {
            mId = ""
            mName = ""
            mPhotoUrl = ""
        })

    }

    override fun onTapContact(contactVO: ContactVO) {

    }

    override fun onSelectContact(isSelect: Boolean, contactVO: ContactVO) {
        if (isSelect) {
            selectedContactList.add(contactVO)
        } else {
            selectedContactList.remove(contactVO)
        }
    }
    private fun getContacts(id: String) {
        mWeChatModel.getContacts(
            id = id,
            onSuccess = {
                mView?.bindContacts(it)
            },
            onFailure = {
                mView?.showError(it)
            }
        )
    }
}