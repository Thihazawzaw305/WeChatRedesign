package com.padcmyanmar.thiha.wechatredesign.mvp.views

import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO

interface MomentView: BaseView {

    fun showPost(momentList: List<MomentVO>)
    fun navigateToCreateMoment()
    fun navigateToLikePeople(millis : String)

}