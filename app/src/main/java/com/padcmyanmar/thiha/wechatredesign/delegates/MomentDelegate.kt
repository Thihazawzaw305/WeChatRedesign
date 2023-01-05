package com.padcmyanmar.thiha.wechatredesign.delegates

interface MomentDelegate {

        fun onTapLike(momentMillis: String,likeCounts: Int,isLike: Boolean,onSuccess : () -> Unit)
        fun onTapBookmark(momentMillis: String, isBookmark: Boolean, onSuccess: () -> Unit)
        fun onTapLikePeople(momentMillis: String,onSuccess: () -> Unit)
    }
