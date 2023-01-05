package com.padcmyanmar.thiha.wechatredesign.data.vos

import com.google.firebase.firestore.IgnoreExtraProperties


data class MomentVO(
   var id: String? ="",
   var description: String? ="",
   var millis: Number? = 0,
   var name: String? ="",
   var photoList: List<String>,
   var videoLink: String ="",
   var isLike: Boolean,
   var isBookmark :Boolean,
   var profileImage: String? = "",
   var likeCounts: Int = 0

    )