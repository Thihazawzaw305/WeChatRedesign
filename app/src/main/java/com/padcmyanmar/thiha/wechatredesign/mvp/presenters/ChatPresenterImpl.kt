package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.ChatView

class ChatPresenterImpl:ViewModel(),ChatPresenter {

    val mWeChatModel = WeChatRedesignModelImpl
    var mId: String = ""
    var normalContactList: ArrayList<ContactVO> = arrayListOf()
    var groupContactList: ArrayList<ContactVO> = arrayListOf()
    lateinit var latestMessageListener: ValueEventListener
    var mView : ChatView ?=null
    private val database: DatabaseReference = Firebase.database.reference

    override fun initView(view: ChatView) {
        mView = view

    }

    override fun onUiReady(owner: LifecycleOwner) {
mWeChatModel.getUser(onSuccess = {
    mId = it.id.toString()
    getContacts(mId)
    getLatestMessage(mId, owner)

}, onFailure =
{
    mId = ""
})
    }

    override fun onTapChat(contactVO: ContactVO) {
        mView?.navigateToChatRoomScreen(contactVO)
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
    private fun getLatestMessage(id: String, owner: LifecycleOwner) {
        mWeChatModel.getLastMessage(
            ownId = id,
            onSuccess = {
                normalContactList.clear()
                normalContactList = ArrayList(it)
                mView?.bindLastMessage(normalContactList.plus(groupContactList))
            },
            onFail = {
                mView?.showError(it)
            }
        )

        mWeChatModel.getGroupLastMessage(
            ownId = id,
            onSuccess = {
                groupContactList.clear()
                groupContactList = ArrayList(it)
                mView?.bindLastMessage(groupContactList.plus(normalContactList))
            },
            onFail = {
                mView?.showError(it)
            }
        )

    }

    private fun getLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    ){

        latestMessageListener = database.child("contactsAndMessages")
            .child(ownId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFail("Cannot get data")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val contactList = arrayListOf<ContactVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        val map = dataSnapShot.value as Map<String, *>
                        Log.d("snap_shot_list", (map["contact"].toString()))

                        if (map["contact"].toString() != "null") {
                            val contactMap = map["contact"] as Map<String, *>
                            val messageMap = map["messages"] as Map<String, *>
                            val lastKey =
                                messageMap.toSortedMap(compareBy<String> { it.length }.thenBy { it })
                                    .lastKey()
                            val latestMessageMap = messageMap[lastKey] as Map<String, *>
                            var latestMessage = ""

                            if (latestMessageMap["text"].toString().isNotEmpty()) {
                                latestMessage = latestMessageMap["text"].toString()
                            } else if (latestMessageMap["photoList"].toString() != "null") {
                                latestMessage = "sent a photo."
                            } else if (latestMessageMap["videoLink"].toString().isNotEmpty()) {
                                latestMessage = "sent a video"
                            }
                            Log.d("snap_shot_list", latestMessage)

                            val contact = ContactVO(
                                id = contactMap["id"].toString(),
                                name = contactMap["name"].toString(),
                                photoUrl = contactMap["photoUrl"].toString(),
                                lastMessage = latestMessage
                            )

                            contactList.add(contact)
                        }

                    }
                    onSuccess(contactList)
//                    contactLiveData.postValue(contactList)

                }
            })


    }

    override fun onCleared() {
        super.onCleared()
        mWeChatModel.removeLatestMessageListener(mId)
//        database.child("contactsAndMessages")
//            .child(mId).removeEventListener(latestMessageListener)
        Log.d("viewModel","clear")
    }
}