package com.padcmyanmar.thiha.wechatredesign.network

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION_CODES.S
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.GroupVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.MessageVO
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

object RealTimeDatabaseApiImpl : RealTimeDatabaseApi {
    private val database: DatabaseReference = Firebase.database.reference
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    private var latestMessageListener: ValueEventListener? = null
    private var latestGroupMessageListener: ValueEventListener? = null
    private var groupListListener: ValueEventListener? = null
    private var chatRoomListener: ValueEventListener? = null
    private var groupChatRoomListener: ValueEventListener? = null


    override fun addMessage(
        contactVO: ContactVO,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {


        val uploadedLinkList: ArrayList<String> = arrayListOf()

        if (fileList.isNotEmpty()) {
            uploadMultiFile(
                fileList = fileList,
                onSuccess = {
                    uploadedLinkList.add(it)
                    Log.d("multi_file_link", it)
                    if (uploadedLinkList.size == fileList.size) {
                        insertMessages(
                            contactVO = contactVO,
                            uploadedLinkList = uploadedLinkList,
                            isMovie = fileList.first().realPath.isNotEmpty(),
                            messageVO = messageVO,
                            onSuccess, onFailure
                        )
                    }
                },
                onFailure = {
                    onFailure(it)
                }
            )
        } else {
            insertMessages(
                contactVO = contactVO,
                uploadedLinkList = uploadedLinkList,
                isMovie = false,
                messageVO = messageVO,
                onSuccess, onFailure
            )
        }
    }

    override fun getMessagesForChatRoom(
        ownId: String,
        otherId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        chatRoomListener = database.child("contactsAndMessages")
            .child(ownId)
            .child(otherId)
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFail(error.message)
                    database.removeEventListener(this)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = arrayListOf<MessageVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        dataSnapShot.getValue(MessageVO::class.java)?.let {
                            messageList.add(it)
                        }
//                        val message = MessageVO(
//                            text =
//                        )

                        Log.d("firebase", dataSnapShot.toString())
                    }
                    onSuccess(messageList)
                }
            })
    }

    override fun removeChatRoomListener(ownId: String,otherId: String) {
        if(chatRoomListener != null){
            database.child("contactsAndMessages")
                .child(ownId)
                .child(otherId)
                .child("messages").removeEventListener(chatRoomListener!!)

            chatRoomListener = null
        }
    }

    override fun getLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    ){
//        val contactLiveData : MutableLiveData<List<ContactVO>> = MutableLiveData()

         latestMessageListener = database.child("contactsAndMessages")
            .child(ownId)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    database.removeEventListener(this)
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

//        return contactLiveData
    }

    override fun removeLatestMessageListener(ownId: String) {
        Log.d("listener","remove listener $ownId")
        if(latestMessageListener != null){
            database.child("contactsAndMessages")
                .child(ownId).removeEventListener(latestMessageListener!!)
            latestMessageListener = null
        }

        if(latestGroupMessageListener != null){
            database.child("groups").removeEventListener(latestGroupMessageListener!!)
            latestGroupMessageListener = null
        }
    }


    override fun getGroupLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
       latestGroupMessageListener = database.child("groups")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    database.removeEventListener(this)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
//                    val contactList = arrayListOf<ContactVO>()
//                    snapshot.children.forEach { dataSnapShot ->
//                        val map = dataSnapShot.value as Map<String, *>
//                        Log.d("snap_shot_list", (map["contact"].toString()))
//
//                        if (map["contact"].toString() != "null") {
//                            val contactMap = map["contact"] as Map<String, *>
//                            val messageMap = map["messages"] as Map<String, *>
//                            val lastKey =
//                                messageMap.toSortedMap(compareBy<String> { it.length }.thenBy { it })
//                                    .lastKey()
//                            val latestMessageMap = messageMap[lastKey] as Map<String, *>
//                            var latestMessage = ""
//
//                            if (latestMessageMap["text"].toString().isNotEmpty()) {
//                                latestMessage = latestMessageMap["text"].toString()
//                            } else if (latestMessageMap["photoList"].toString() != "null") {
//                                latestMessage = "sent a photo."
//                            } else if (latestMessageMap["videoLink"].toString().isNotEmpty()) {
//                                latestMessage = "sent a video"
//                            }
//                            Log.d("snap_shot_list", latestMessage)
//
//                            val contact = ContactVO(
//                                id = contactMap["id"].toString(),
//                                name = contactMap["name"].toString(),
//                                photoUrl = contactMap["photoUrl"].toString(),
//                                lastMessage = latestMessage
//                            )
//
//                            contactList.add(contact)
//                        }

//                    }
//                    onSuccess(contactList)

                    val contactList = arrayListOf<ContactVO>()
                    val groupList = arrayListOf<GroupVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        val map = dataSnapShot.value as Map<String, *>
                        dataSnapShot.getValue(GroupVO::class.java)?.let {
                            if (it.members.any { contact -> contact.id == ownId }) {
                                groupList.add(it)
                            }
                        }
                    }
                    Log.d("group_history",groupList.toString())

                    groupList.forEach {
                        if (it.messages.isNotEmpty()) {
                            val lastKey =
                                it.messages.toSortedMap(compareBy<String> { it.length }.thenBy { it })
                                    .lastKey()
                            val latestMessageMap = it.messages[lastKey] as Map<String, *>
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
                                id = it.id,
                                name = it.name,
                                photoUrl = it.photo,
                                lastMessage = latestMessage,
                                isGroup = true
                            )
                            contactList.add(contact)
                        }
                    }
                    onSuccess(contactList)
                }
            })
    }

    override fun createGroup(
        name: String,
        bitmap: Bitmap,
        contactList: List<ContactVO>,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        uploadMultiFile(
            fileList = listOf(FileVO(bitmap = bitmap, isMovie = false, uri = Uri.EMPTY)),
            onSuccess = { groupImageLink ->
                insertGroup(
                    name = name,
                    imageLink = groupImageLink,
                    contactList = contactList,
                    onSuccess = onSuccess,
                    onFailure = onFail
                )
            },
            onFailure = onFail
        )
    }

    override fun getGroups(
        selfId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
       groupListListener = database.child("groups")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFail(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val groupList = arrayListOf<GroupVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        dataSnapShot.getValue(GroupVO::class.java)?.let {
                            if (it.members.any { contact -> contact.id == selfId }) {
                                groupList.add(it)
                            }
                        }
//                        val message = MessageVO(
//                            text =
//                        )

                        Log.d("firebase", dataSnapShot.toString())
                    }
                    onSuccess(groupList)
                }
            })
    }

    override fun removeGroupListListener(selfId: String) {
       if(groupListListener != null){
           database.child("groups").removeEventListener(groupListListener!!)
           groupListListener = null
       }
    }

    override fun getGroupMessages(
        groupId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        groupChatRoomListener = database.child("groups")
            .child(groupId)
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFail(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = arrayListOf<MessageVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        dataSnapShot.getValue(MessageVO::class.java)?.let {
                            messageList.add(it)
                        }
//                        val message = MessageVO(
//                            text =
//                        )

                        Log.d("firebase", dataSnapShot.toString())
                    }
                    onSuccess(messageList)
                }
            })
    }

    override fun removeGroupChatRoomListener(groupId: String) {
        if(groupChatRoomListener != null){
            database.child("groups")
                .child(groupId)
                .child("messages").removeEventListener(groupChatRoomListener!!)

            groupChatRoomListener = null
        }
    }

    override fun addMessageToGroup(
        groupId: String,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val uploadedLinkList: ArrayList<String> = arrayListOf()

        if (fileList.isNotEmpty()) {
            uploadMultiFile(
                fileList = fileList,
                onSuccess = {
                    uploadedLinkList.add(it)
                    Log.d("multi_file_link", it)
                    if (uploadedLinkList.size == fileList.size) {
                        insertGroupMessage(
                            groupId = groupId,
                            uploadedLinkList = uploadedLinkList,
                            isMovie = fileList.first().realPath.isNotEmpty(),
                            messageVO = messageVO,
                            onSuccess, onFailure
                        )
                    }
                },
                onFailure = {
                    onFailure(it)
                }
            )
        } else {
            insertGroupMessage(
                groupId = groupId,
                uploadedLinkList = uploadedLinkList,
                isMovie = false,
                messageVO = messageVO,
                onSuccess, onFailure
            )
        }

    }


    private fun insertGroup(
        name: String,
        imageLink: String,
        contactList: List<ContactVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val groupVO = GroupVO(
            id = System.currentTimeMillis().toString(),
            name = name,
            photo = imageLink,
            members = contactList,
        )
        database.child("groups").child(groupVO.id.toString())
            .setValue(groupVO)
            .addOnCompleteListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "create group error.")
                Log.d("firebase", it.message ?: "unknown")
            }

    }


    private fun uploadMultiFile(
        fileList: List<FileVO>,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {

        fileList.forEach {
            if (!it.isMovie) {
                val baos = ByteArrayOutputStream()
                it.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val imageRef = storageReference.child("files/${UUID.randomUUID()}")
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    onFailure(it.message ?: "Upload image to firebase storage failed.")
                }.addOnSuccessListener { taskSnapshot ->

                }

                val urlTask = uploadTask.continueWithTask {
                    return@continueWithTask imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    val imageUrl = task.result?.toString()
                    onSuccess(imageUrl.toString())
                }
            } else {
                var file = Uri.fromFile(File(it.realPath))
                val videoRef = storageReference.child("videos/${file.lastPathSegment}")

                val uploadTask = videoRef.putFile(file)
                uploadTask.addOnFailureListener {
                    onFailure(it.message ?: "Upload video to firebase storage failed.")
                }.addOnSuccessListener { taskSnapshot ->

                }

                val urlTask = uploadTask.continueWithTask {
                    return@continueWithTask videoRef.downloadUrl
                }.addOnCompleteListener { task ->
                    val imageUrl = task.result?.toString()
                    onSuccess(imageUrl.toString())
                }


            }

        }
    }


    private fun insertMessages(
        contactVO: ContactVO,
        uploadedLinkList: List<String>,
        isMovie: Boolean,
        messageVO: MessageVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {

        var photoList: List<String> = listOf()
        var movieLink: String = ""

        if (isMovie) {
            movieLink = uploadedLinkList.firstOrNull() ?: ""
        } else {
            photoList = uploadedLinkList
        }

        val message = messageVO.copy(
            videoLink = movieLink,
            photoList = ArrayList(photoList)
        )

        Log.d("message", message.toString())

        var ownMessageSuccess = false
        var otherMessageSuccess = false
        var insertContactSuccess = false

        database.child("contactsAndMessages").child(messageVO.id)
            .child(contactVO.id)
            .child("messages")
            .child(message.millis.toString())
            .setValue(message)
            .addOnCompleteListener {
                ownMessageSuccess = true
                if (insertContactSuccess && otherMessageSuccess) {
                    onSuccess()
                }

            }
            .addOnFailureListener {
                onFailure(it.message ?: "message insert error.")
                Log.d("firebase", it.message ?: "unknown")
            }

        database.child("contactsAndMessages").child(contactVO.id)
            .child(message.id)
            .child("messages")
            .child(message.millis.toString())
            .setValue(message)
            .addOnCompleteListener {
                otherMessageSuccess = true
                if (ownMessageSuccess && insertContactSuccess) {
                    onSuccess()
                }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "message insert error.")
                Log.d("firebase", it.message ?: "unknown")
            }

        val selfContact = ContactVO(
            name = message.name,
            id = message.id,
            photoUrl = message.profileImage,
        )

        insertContact(
            selfContact = selfContact,
            otherContact = contactVO,
            onSuccess = {
                insertContactSuccess = true
                if (ownMessageSuccess && otherMessageSuccess) {
                    onSuccess()
                }
            },
            onFailure = {
                onFailure(it)
            }
        )


    }

    private fun insertContact(
        selfContact: ContactVO, otherContact: ContactVO, onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {

        var selfContactSuccess = false
        var otherContactSuccess = false


        database.child("contactsAndMessages").child(selfContact.id)
            .child(otherContact.id)
            .child("contact")
            .setValue(otherContact)
            .addOnCompleteListener {
                selfContactSuccess = true
                if (otherContactSuccess) {
                    onSuccess()
                }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "insert contact failed")
                Log.d("firebase", it.message ?: "insert contact failed")
            }

        database.child("contactsAndMessages").child(otherContact.id)
            .child(selfContact.id)
            .child("contact")
            .setValue(selfContact)
            .addOnCompleteListener {
                otherContactSuccess = true
                if (selfContactSuccess) {
                    onSuccess()
                }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "insert contact failed")
                Log.d("firebase", it.message ?: "insert contact failed")
            }

    }

    private fun insertGroupMessage(
        groupId: String,
        uploadedLinkList: List<String>,
        isMovie: Boolean,
        messageVO: MessageVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {

        var photoList: List<String> = listOf()
        var movieLink: String = ""

        if (isMovie) {
            movieLink = uploadedLinkList.firstOrNull() ?: ""
        } else {
            photoList = uploadedLinkList
        }

        val message = messageVO.copy(
            videoLink = movieLink,
            photoList = ArrayList(photoList)
        )

        Log.d("message", message.toString())


        database.child("groups")
            .child(groupId)
            .child("messages")
            .child(System.currentTimeMillis().toString())
            .setValue(message)
            .addOnCompleteListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "message insert error.")
                Log.d("firebase", it.message ?: "unknown")
            }


    }


}