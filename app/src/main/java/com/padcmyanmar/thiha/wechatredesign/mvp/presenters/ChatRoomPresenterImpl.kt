package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import CONTENT
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.MessageVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.ChatRoomView

class ChatRoomPresenterImpl:ViewModel(),ChatRoomPresenter {
    val mWeChatModel = WeChatRedesignModelImpl
    var mView : ChatRoomView ?= null
    var mId: String = ""
    var mName: String = ""
    var mPhotoUrl: String = ""
    var mIsGroup: Boolean = false
    var mOtherId: String = ""
    override fun initView(view: ChatRoomView) {
     mView = view
    }

    override fun onUiReadyWithId(
        context: Context,
        owner: LifecycleOwner,
        otherId: String,
        isGroup: Boolean
    ) {
        mIsGroup = isGroup
        mOtherId = otherId
        mWeChatModel.getUser(onSuccess = {
            mId =it.id.toString()
            mName = it.name.toString()
            mPhotoUrl = it.profileUrl.toString()
            if(!isGroup){
                mWeChatModel.getMessagesForChatRoom(
                    ownId = mId,
                    otherId = otherId,
                    onSuccess = { messages ->
                        mView?.bindMessages(ownId = mId, messageList = messages)
                    },
                    onFail = { error ->
                        mView?.showError(error)
                    }
                )
            }else{
                mWeChatModel.getGroupMessages(
                    groupId = otherId,
                    onSuccess = { messages ->
                        mView?.bindMessages(ownId = mId, messageList = messages)
                    },
                    onFail = { error ->
                        mView?.showError(error)
                    }
                )
            }
        }, onFailure = {
            mId =""
            mName =""
            mPhotoUrl = ""

        })
    }

    override fun sentMessage(contactVO: ContactVO, fileList: List<FileVO>, message: MessageVO) {
        Log.d("firebase", "call sent message")
        if(!mIsGroup){
            mWeChatModel.addMessage(
                contactVO = contactVO,
                messageVO = MessageVO(
                    text = message.text,
                    millis = message.millis,
                    photoList = message.photoList,
                    videoLink = message.videoLink,
                    name = mName,
                    id = mId,
                    profileImage = mPhotoUrl
                ),
                fileList = fileList,
                onSuccess = {

                },
                onFailure = {
                    mView?.showError(it)
                }
            )
        }else{
            mWeChatModel.addMessageToGroup(
                groupId = contactVO.id,
                messageVO = MessageVO(
                    text = message.text,
                    millis = message.millis,
                    photoList = message.photoList,
                    videoLink = message.videoLink,
                    name = mName,
                    id = mId,
                    profileImage = mPhotoUrl
                ),
                fileList = fileList,
                onSuccess = {

                },
                onFailure = {
                    mView?.showError(it)
                }
            )

        }
    }

    override fun onTapBack() {
        mView?.navigateBack()
    }

    override fun onTapCamera() {
        mView?.navigateToCameraScreen()
    }

    override fun onUiReady(owner: LifecycleOwner) {

    }

    override fun onTapContent(content: CONTENT) {
        mView?.onTapContent(content)
    }

    override fun onTapDelete(fileVO: FileVO) {
        mView?.onFileRemove(fileVO)
    }
}