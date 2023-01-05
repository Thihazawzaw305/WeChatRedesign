package com.padcmyanmar.thiha.wechatredesign.data.models

import android.graphics.Bitmap
import com.padcmyanmar.thiha.wechatredesign.data.vos.*
import com.padcmyanmar.thiha.wechatredesign.network.FirebaseApi

interface WeChatRedesignModel {
    var mFirebaseApi: FirebaseApi
    fun getMoment(id: String,onSuccess: (moment: List<MomentVO>) -> Unit, onFailure: (String) -> Unit)

    fun getUser(onSuccess: (user: UserVO) -> Unit, onFailure: (String) -> Unit)
    fun addUser(
        phoneNumber: String,
        name: String,
        dateOfBirth: String,
        gender: String,
        profileUrl: String
    )

    fun upLoadProfile(image: Bitmap, user: UserVO)
    fun uploadMoment(
        description: String,
        fileList: List<FileVO>,
        id: String,
        name: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
    fun giveLike(   like: Boolean,
                    momentMillis: String,
                    id: String,
                    likeCounts : Int,
                    onSuccess: () -> Unit,
                    onFailure: (String) -> Unit)

   fun bookMarkMoment(
    BookMark: Boolean,
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

    fun addMessage(
        contactVO: ContactVO,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getMessagesForChatRoom(
        ownId: String,
        otherId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun removeChatRoomListener(ownId: String,otherId: String)

    fun getLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun getGroupLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun removeLatestMessageListener(ownId: String)

    fun createGroup(
        name: String,
        bitmap: Bitmap,
        contactList: List<ContactVO>,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    )

    fun getGroups(
        selfId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun removeGroupListListener(selfId: String)

    fun getGroupMessages(
        groupId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun removeGroupChatRoomListener(groupId: String)

    fun addMessageToGroup(
        groupId: String,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}