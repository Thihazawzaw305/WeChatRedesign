package com.padcmyanmar.thiha.wechatredesign.data.models

import android.graphics.Bitmap
import com.padcmyanmar.thiha.wechatredesign.data.vos.*
import com.padcmyanmar.thiha.wechatredesign.network.CloudFirestoreFirebaseApiImpl
import com.padcmyanmar.thiha.wechatredesign.network.FirebaseApi
import com.padcmyanmar.thiha.wechatredesign.network.RealTimeDatabaseApi
import com.padcmyanmar.thiha.wechatredesign.network.RealTimeDatabaseApiImpl


object WeChatRedesignModelImpl : WeChatRedesignModel {
    override var mFirebaseApi: FirebaseApi = CloudFirestoreFirebaseApiImpl
    var mRealTimeDatabaseApi: RealTimeDatabaseApi = RealTimeDatabaseApiImpl
    override fun getMoment(
        id: String,
        onSuccess: (moment: List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mFirebaseApi.getMoment(id,onSuccess, onFailure)
    }

    override fun getUser(onSuccess: (user: UserVO) -> Unit, onFailure: (String) -> Unit) {
        mFirebaseApi.getUserInfo(onSuccess, onFailure)
    }

    override fun addUser(

        phoneNumber: String,
        name: String,
        dateOfBirth: String,
        gender: String,
        profileUrl: String
    ) {
        mFirebaseApi.createUser(phoneNumber, name, dateOfBirth, gender, profileUrl)
    }

    override fun upLoadProfile(image: Bitmap, user: UserVO) {
        mFirebaseApi.upLoadProfile(image, user)
    }

    override fun uploadMoment(
        description: String,
        fileList: List<FileVO>,
        id: String,
        name: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mFirebaseApi.uploadMoment(description, fileList, id, name, profileImage, onSuccess, onFailure)
    }

    override fun giveLike(
        like: Boolean,
        momentMillis: String,
        id: String,
        likeCounts: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
      mFirebaseApi.giveLike(like, momentMillis, id, likeCounts, onSuccess, onFailure)
    }

    override fun bookMarkMoment(
        BookMark: Boolean,
        momentMillis: String,
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
       mFirebaseApi.bookMarkMoment(BookMark, momentMillis, id, onSuccess, onFailure)
    }

    override fun getBookMarkMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
      mFirebaseApi.getBookMarkMoments(id, onSuccess, onFailure)
    }

    override fun updateUser(
        id: String,
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        mFirebaseApi.updateUser(id, name, phone, password, dob, gender, profileImage, onSuccess, onFailure)
    }

    override fun addContacts(
        selfId: String,
        friendId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        mFirebaseApi.addContacts(selfId, friendId, onSuccess, onFailure)
    }

    override fun getContacts(
        id: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
       mFirebaseApi.getContacts(id, onSuccess, onFailure)
    }

    override fun addMessage(
        contactVO: ContactVO,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.addMessage(contactVO, messageVO, fileList, onSuccess, onFailure)
    }

    override fun getMessagesForChatRoom(
        ownId: String,
        otherId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getMessagesForChatRoom(ownId, otherId, onSuccess, onFail)
    }

    override fun removeChatRoomListener(ownId: String, otherId: String) {
        mRealTimeDatabaseApi.removeChatRoomListener(ownId, otherId)
    }

    override fun getLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getLastMessage(ownId,onSuccess, onFail)
    }

    override fun getGroupLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getGroupLastMessage(ownId, onSuccess, onFail)
    }

    override fun removeLatestMessageListener(ownId: String) {
        mRealTimeDatabaseApi.removeLatestMessageListener(ownId)
    }

    override fun createGroup(
        name: String,
        bitmap: Bitmap,
        contactList: List<ContactVO>,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.createGroup(name, bitmap, contactList, onSuccess, onFail)
    }

    override fun getGroups(
        selfId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getGroups(selfId, onSuccess, onFail)
    }

    override fun removeGroupListListener(selfId: String) {
        mRealTimeDatabaseApi.removeGroupListListener(selfId)
    }

    override fun getGroupMessages(
        groupId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getGroupMessages(groupId, onSuccess, onFail)
    }

    override fun removeGroupChatRoomListener(groupId: String) {
        mRealTimeDatabaseApi.removeGroupChatRoomListener(groupId)
    }

    override fun addMessageToGroup(
        groupId: String,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.addMessageToGroup(groupId, messageVO, fileList, onSuccess, onFailure)
    }


}