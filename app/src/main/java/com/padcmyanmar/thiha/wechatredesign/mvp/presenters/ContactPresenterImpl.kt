package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.GroupVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.ContactView

class ContactPresenterImpl: ViewModel(),ContactPresenter {
  var  mView : ContactView ?= null
    var mId: String = ""
    val mWeChatModel = WeChatRedesignModelImpl
    override fun initView(view: ContactView) {
     mView = view
    }

    override fun onTapAddContact() {
        mView?.navigateToQrScannerScreen()
    }

    override fun onTapCreateGroup() {
        mView?.navigateToCreateGroupScreen()
    }

    override fun refreshGroupList() {
        getGroups(mId)
    }

    override fun addContact(friendId: String) {
        mWeChatModel.addContacts(
            selfId = mId,
            friendId = friendId,
            onSuccess = {
                getContacts(mId)
            },
            onFailure = {
                mView?.showError(it)
            }
        )
    }

    override fun onUiReady(owner: LifecycleOwner) {
       mWeChatModel.getUser(onSuccess = {
           mId = it.id.toString()
           getContacts(mId)
           getGroups(mId)
       }, onFailure = {

       })
    }

    override fun onTapContact(contactVO: ContactVO) {
        mView?.navigateToChatRoomScreen(contactVO)
    }

    override fun onSelectContact(isSelect: Boolean, contactVO: ContactVO) {

    }

    override fun onTapGroup(groupVO: GroupVO) {
        mView?.navigateToGroupChatScreen(groupVO)
    }


    private fun getContacts(id: String){
        mWeChatModel.getContacts(
            id = mId,
            onSuccess = {
                mView?.bindContacts(it)
            },
            onFailure = {
                mView?.showError(it)
            }
        )
    }

    private fun getGroups(id: String){
        mWeChatModel.getGroups(
            selfId = mId,
            onSuccess = {
                mView?.bindGroups(it)
            },
            onFail = {
                mView?.showError(it)
            }
        )
    }
    override fun onCleared() {
        super.onCleared()
        mWeChatModel.removeGroupListListener(selfId = mId)
    }
}