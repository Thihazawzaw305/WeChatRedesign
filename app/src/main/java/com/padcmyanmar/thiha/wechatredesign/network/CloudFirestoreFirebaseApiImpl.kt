package com.padcmyanmar.thiha.wechatredesign.network

import FIRE_STORE_REF_DOB
import FIRE_STORE_REF_GENDER
import FIRE_STORE_REF_ID
import FIRE_STORE_REF_NAME
import FIRE_STORE_REF_PHONE
import FIRE_STORE_REF_PROFILE_IMAGE
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

object CloudFirestoreFirebaseApiImpl : FirebaseApi {

    val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun getUserInfo(onSuccess: (user: UserVO) -> Unit, onFailure: (String) -> Unit) {
        db.collection("users")
            .document(mFirebaseAuth.currentUser?.uid.toString())
            .addSnapshotListener { data, error ->
                error?.let {
                    onFailure(it.message ?: "Please check connection")
                } ?: run {

                    val user = UserVO()
                    user.id = data?.get("id") as String
                    user.dateOfBirth = data["dateOfBirth"] as String
                    user.phoneNumber = data["phoneNumber"] as String
                    user.gender = data["gender"] as String
                    user.name = data["name"] as String
                    user.profileUrl = data["profileUrl"] as String

                    onSuccess(user)
                }

            }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMoment(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collectionGroup("likes")
            .whereEqualTo("id", id)
            .get()
            .addOnCompleteListener {
                val documents = it.result?.documents ?: listOf()
                val likeMoments: ArrayList<String> = arrayListOf()

                documents.forEach { document ->
                    val data = document.data
                    likeMoments.add(data?.get("millis") as String)
                }
                Log.d("check", likeMoments.toString())

                db.collectionGroup("bookmarks")
                    .whereEqualTo("id", id)
                    .get()
                    .addOnCompleteListener {
                        val bmDocuments = it.result?.documents ?: listOf()
                        val bookMarkMoments: ArrayList<String> = arrayListOf()

                        bmDocuments.forEach { document ->
                            val data = document.data
                            bookMarkMoments.add(data?.get("millis") as String)
                        }
                        Log.d("moments", bookMarkMoments.toString())



                        db.collection("posts")
                            .get()
                            .addOnCompleteListener {

                                val momentList: MutableList<MomentVO> = arrayListOf()

                                val result = it.result?.documents ?: arrayListOf()

                                for (document in result) {
                                    val data = document.data
                                    val moment = MomentVO(
                                        description = data?.get("description") as String,
                                        millis = data["millis"] as Long,
                                        name = data["name"] as String,
                                        profileImage = data["profileImage"] as String,
                                        id = data["id"] as String,
                                        photoList = data["photoList"] as List<String>,
                                        videoLink = data["videoLink"] as String,
                                        isLike = likeMoments.contains(data["millis"].toString()),
                                        isBookmark = bookMarkMoments.contains(data["millis"].toString()),
                                        likeCounts = Math.toIntExact(
                                            (data["likeCounts"] ?: 0L) as Long
                                        ),
                                    )
                                    momentList.add(moment)
                                }
                                onSuccess(momentList.reversed())
                            }.addOnFailureListener { error ->
                                onFailure(error.message ?: "moments fetch failed.")
                            }
                    }
            }

    }


    override fun giveLike(
        like: Boolean,
        momentMillis: String,
        id: String,
        likeCounts: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = mapOf<String, Any>(
            "millis" to momentMillis,
            "id" to id,
        )

        if (like) {
            db.collection("posts")
                .document(momentMillis)
                .collection("likes")
                .document(id)
                .set(userMap)
                .addOnCompleteListener {
                    updateLikeCount(likeCounts + 1, momentMillis, onSuccess, onFailure)
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "like reaction failed.")
                }
        } else {
            db.collection("posts")
                .document(momentMillis)
                .collection("likes")
                .document(id)
                .delete()
                .addOnCompleteListener {
                    updateLikeCount(likeCounts - 1, momentMillis, onSuccess, onFailure)
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "like reaction failed.")
                }
        }


    }

    override fun bookMarkMoment(
        isBookMark: Boolean,
        momentMillis: String,
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = mapOf<String, Any>(
            "millis" to momentMillis,
            "id" to id,
        )

        if (isBookMark) {
            db.collection("posts")
                .document(momentMillis)
                .collection("bookmarks")
                .document(id)
                .set(userMap)
                .addOnCompleteListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "bookmark failed.")
                }
        } else {
            db.collection("posts")
                .document(momentMillis)
                .collection("bookmarks")
                .document(id)
                .delete()
                .addOnCompleteListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "bookmark failed.")
                }
        }
    }


    override fun createUser(

        phoneNumber: String,
        name: String,
        dateOfBirth: String,
        gender: String,
        profileUrl: String
    ) {
        val mid = mFirebaseAuth.currentUser?.uid.toString()
        val userMap = hashMapOf(
            "id" to mid,
            "phoneNumber" to phoneNumber,
            "name" to name,
            "dateOfBirth" to dateOfBirth,
            "gender" to gender,
            "profileUrl" to profileUrl
        )
        db.collection("users")
            .document(mid)
            .set(userMap)
            .addOnSuccessListener { Log.d("Success", "Successfully added grocery") }
            .addOnFailureListener { Log.d("Failure", "Failed to add grocery") }
    }


    override fun upLoadProfile(image: Bitmap, user: UserVO) {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            //
        }.addOnSuccessListener { taskSnapshot ->
            //
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val imageUrl = task.result?.toString()
            updateProfile(imageUrl.toString(), onSuccess = {

            }, onFailure = {

            })
        }
    }

    override fun updateProfile(
        profileImage: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(mFirebaseAuth.currentUser?.uid.toString())
            .update("profileUrl", profileImage)
            .addOnSuccessListener {
                onSuccess(profileImage)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "error")
            }

//        db.collection("users")
//            .document(mFirebaseAuth.currentUser?.uid.toString())
//            .collection("userPosts")
//            .get()
//            .addOnCompleteListener {
//                val result = it.result?.documents ?: arrayListOf()
//                for (document in result) {
//                    val data = document.data
//                    val post = data?.get("id") as String
//                    db.collection("posts")
//                        .document(post)
//                        .update("profileImage", profileImage)
//                        .addOnSuccessListener {
//                            onSuccess(profileImage)
//                        }
//                        .addOnFailureListener {
//                            onFailure(it.message ?: "error")
//                        }
//                }
//            }
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
        val uploadedLinkList: ArrayList<String> = arrayListOf()

        if (fileList.isNotEmpty()) {
            uploadMultiFile(
                fileList = fileList,
                onSuccess = {
                    uploadedLinkList.add(it)
                    Log.d("multi_file_link", it)
                    if (uploadedLinkList.size == fileList.size) {
                        insertMoment(
                            description,
                            uploadedLinkList,
                            fileList.first().realPath.isNotEmpty(),
                            name, id, profileImage, onSuccess, onFailure
                        )
                    }
                },
                onFailure = {
                    onFailure(it)
                }
            )
        } else {
            insertMoment(
                description,
                uploadedLinkList,
                false,
                name, id, profileImage, onSuccess, onFailure
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getBookMarkMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collectionGroup("bookmarks")
            .whereEqualTo("id", id)
            .get()
            .addOnCompleteListener {
                val bmDocuments = it.result?.documents ?: listOf()
                val bookMarkMoments: ArrayList<String> = arrayListOf()

                bmDocuments.forEach { document ->
                    val data = document.data
                    bookMarkMoments.add(data?.get("millis") as String)
                }
                Log.d("moments", bookMarkMoments.toString())

                db.collectionGroup("likes")
                    .whereEqualTo("id", id)
                    .get()
                    .addOnCompleteListener {
                        val documents = it.result?.documents ?: listOf()
                        val likeMoments: ArrayList<String> = arrayListOf()

                        documents.forEach { document ->
                            val data = document.data
                            likeMoments.add(data?.get("millis") as String)
                        }
                        Log.d("moments", likeMoments.toString())

                        db.collection("posts")
                            .get()
                            .addOnCompleteListener {

                                val momentList: MutableList<MomentVO> = arrayListOf()

                                val result = it.result?.documents ?: arrayListOf()

                                for (document in result) {
                                    val data = document.data
                                    val moment = MomentVO(
                                        description = data?.get("description") as String,
                                        millis = data["millis"] as Long,
                                        name = data["name"] as String,
                                        profileImage = data["profileImage"] as String,
                                        id = data["id"] as String,
                                        photoList = data["photoList"] as List<String>,
                                        videoLink = data["videoLink"] as String,
                                        isLike = likeMoments.contains(data["millis"].toString()),
                                        isBookmark = bookMarkMoments.contains(data["millis"].toString()),
                                        likeCounts = Math.toIntExact(
                                            (data["likeCounts"] ?: 0L) as Long
                                        )

                                    )
                                    if (moment.isBookmark) {
                                        momentList.add(moment)
                                    }
                                }
                                onSuccess(momentList.reversed())
                            }.addOnFailureListener { error ->
                                onFailure(error.message ?: "moments fetch failed.")
                            }
                    }


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

    private fun insertMoment(
        description: String,
        uploadedLinkList: List<String>,
        isMovie: Boolean,
        name: String,
        id: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        var photoList: List<String> = listOf()
        var videoLink: String = ""
        val millis = System.currentTimeMillis()

        if (isMovie) {
            videoLink = uploadedLinkList.firstOrNull() ?: ""
        } else {
            photoList = uploadedLinkList
        }
        val momentMap = hashMapOf(
            "millis" to millis,
            "description" to description,
            "uploadedLinkList" to uploadedLinkList,
            "videoLink" to videoLink,
            "photoList" to photoList,
            "name" to name,
            "id" to id,
            "profileImage" to profileImage
        )

        db.collection("posts")
            .document(millis.toString())
            .set(momentMap)
            .addOnSuccessListener {

                onSuccess()
                Log.d("Success", "Successfully added moment")
            }
            .addOnFailureListener {

                onFailure(it.message ?: "Failed to add moment to fire store.")
                Log.d("Failure", "Failed to add moment")
            }

//        val post = hashMapOf(
//            "id" to millis
//        )
//
//        db.collection("users")
//            .document(mFirebaseAuth.currentUser?.uid.toString())
//            .collection("userPosts")
//            .document(millis.toString())
//            .set(post)
//            .addOnCompleteListener {
//                Log.d("Success", "Successfully added userPosts")
//            }
//            .addOnFailureListener {
//                Log.d("Failure", "Failed to add userPosts")
//            }

    }

    private fun updateLikeCount(
        likeCounts: Int,
        momentMillis: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("posts")
            .document(momentMillis)
            .update("likeCounts", likeCounts)
            .addOnCompleteListener {
                onSuccess()
            }.addOnFailureListener {
                onFailure(it.message ?: "like count update failed")
            }
    }


    override fun getLikePeople(
        millis: String,
        onSuccess: (user: List<UserVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
//        db.collection("posts")
//            .document(millis)
//            .collection("likes")
//            .get()
//            .addOnCompleteListener {
//                val result = it.result?.documents ?: arrayListOf()
//                val userList: MutableList<UserVO> = arrayListOf()
//                var user = UserVO()
//                for (document in result) {
//
//                    val data = document.data
//                    val id = data?.get("id") as String
//                    getUser(id, onSuccess = {
//                        userList.add(it)
//
//                    }, onFailure = {
//
//                    })
//
//                }
//
//                onSuccess(userList)
//
//            }.addOnFailureListener {
//                onFailure(
//                    ""
//                )
//            }

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
        val userMap = hashMapOf(
//            FIRE_STORE_REF_ID to id,
//            FIRE_STORE_REF_NAME to name,
//            FIRE_STORE_REF_PHONE to phone,
//            FIRE_STORE_REF_PASSWORD to password,
//            FIRE_STORE_REF_DOB to dob,
//            FIRE_STORE_REF_GENDER to gender,
//            FIRE_STORE_REF_PROFILE_IMAGE to profileImage
            "id" to id,
            "phoneNumber" to phone,
            "name" to name,
            "dateOfBirth" to dob,
            "gender" to gender,
            "profileUrl" to profileImage
        )

        db.collection("users")
            .document(id)
            .set(userMap)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Success", "Successfully added user")
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to add user to fire store.")
                Log.d("Failure", "Failed to add user")
            }
    }

    override fun addContacts(
        selfId: String,
        friendId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        var selfInsertSuccess = false
        var friendInsertSuccess = false

        db.collection("users")
            .document(friendId)
            .get()
            .addOnCompleteListener {
                val map = it.result?.data
                insertUserToContact(
                    selfId = selfId,
                    contactId = friendId,
                    contactName = map?.get(FIRE_STORE_REF_NAME) as String,
                    contactPhone = map[FIRE_STORE_REF_PHONE] as String,
                    contactDob = map[FIRE_STORE_REF_DOB] as String,
                    contactGender = map[FIRE_STORE_REF_GENDER] as String,
                    contactProfile = map[FIRE_STORE_REF_PROFILE_IMAGE] as String,
                    onSuccess = {
                        selfInsertSuccess = true
                        if (selfInsertSuccess && friendInsertSuccess) {
                            onSuccess()
                        }
                    }, onFailure
                )
            }.addOnFailureListener {
                onFailure(it.message ?: "self insert failed.")
            }

        db.collection("users")
            .document(selfId)
            .get()
            .addOnCompleteListener {
                val map = it.result?.data
                insertUserToContact(
                    selfId = friendId,
                    contactId = selfId,
                    contactName = map?.get(FIRE_STORE_REF_NAME) as String,
                    contactPhone = map[FIRE_STORE_REF_PHONE] as String,
                    contactDob = map[FIRE_STORE_REF_DOB] as String,
                    contactGender = map[FIRE_STORE_REF_GENDER] as String,
                    contactProfile = map[FIRE_STORE_REF_PROFILE_IMAGE] as String,
                    onSuccess = {
                        friendInsertSuccess = true
                        if (selfInsertSuccess && friendInsertSuccess) {
                            onSuccess()
                        }
                    }, onFailure
                )
            }.addOnFailureListener {
                onFailure(it.message ?: "self insert failed.")
            }
    }

    override fun getContacts(
        id: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(id)
            .collection("contacts")
            .get()
            .addOnCompleteListener {
                val documents = it.result?.documents ?: listOf()
                val contactList = arrayListOf<ContactVO>()

                documents.forEach {
                    val data = it.data
                    contactList.add(
                        ContactVO(
                            id = data?.get("id") as String,
                            name = data["name"] as String,
                            photoUrl = data["profileUrl"] as String
                        )
                    )
                }
                onSuccess(contactList)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "fail to get contacts.")
            }
    }


    fun getUser(id: String, onSuccess: (user: UserVO) -> Unit, onFailure: (String) -> Unit) {
        db.collection("users")
            .document(id)
            .addSnapshotListener { data, error ->
                error?.let {

                    onFailure(it.message ?: "Please check connection")
                } ?: run {

                    val user = UserVO(
                        name = data?.get("name") as String,
                        profileUrl = data["profileUrl"] as String
                    )
//                    user.name = data?.get("name") as String
//                    user.profileUrl = data["profileUrl"] as String
                    onSuccess(user)
                }

            }
    }



    private fun insertUserToContact(
        selfId: String,
        contactId: String,
        contactName: String,
        contactPhone: String,
        contactDob: String,
        contactGender: String,
        contactProfile: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = hashMapOf(
            FIRE_STORE_REF_ID to contactId,
            FIRE_STORE_REF_NAME to contactName,
            FIRE_STORE_REF_PHONE to contactPhone,
            FIRE_STORE_REF_DOB to contactDob,
            FIRE_STORE_REF_GENDER to contactGender,
            FIRE_STORE_REF_PROFILE_IMAGE to contactProfile
        )

        db.collection("users")
            .document(selfId)
            .collection("contacts")
            .document(contactId)
            .set(userMap)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Success", "Successfully added user")
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to add user to fire store.")
                Log.d("Failure", "Failed to add user")
            }
    }
}

//val result = it.result?.documents ?: arrayListOf()
//val UserList: MutableList<UserVO> = arrayListOf()
//for (document in result) {
//    val data = document.data
//    val user = UserVO(
//        name = data?.get("name") as String,
//        profileUrl = data["profileUrl"] as String
//    )
//    UserList.add(user)
//}