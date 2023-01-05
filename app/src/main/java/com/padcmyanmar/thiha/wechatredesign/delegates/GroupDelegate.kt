package com.padcmyanmar.thiha.wechatredesign.delegates

import com.padcmyanmar.thiha.wechatredesign.data.vos.GroupVO

interface GroupDelegate {
    fun onTapGroup(groupVO: GroupVO)
}