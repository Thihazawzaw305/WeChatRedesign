package com.padcmyanmar.thiha.wechatredesign.mvp.views

import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.GroupVO

interface ContactView : BaseView {
    fun navigateToQrScannerScreen()
    fun navigateToCreateGroupScreen()
    fun bindContacts(contactList: List<ContactVO>)
    fun bindGroups(groupList: List<GroupVO>)
    fun navigateToGroupChatScreen(groupVO: GroupVO)
    fun navigateToChatRoomScreen(contactVO: ContactVO)
}