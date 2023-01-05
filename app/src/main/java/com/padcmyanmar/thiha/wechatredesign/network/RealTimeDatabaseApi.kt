package com.padcmyanmar.thiha.wechatredesign.network

import android.graphics.Bitmap
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.GroupVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.MessageVO


interface RealTimeDatabaseApi {

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