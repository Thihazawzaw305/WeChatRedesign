package com.padcmyanmar.thiha.wechatredesign.data.vos

import CONTENT


data class ContentVO(
    val content: CONTENT,
    val image: Int,
    var isSelected: Boolean = false
)

