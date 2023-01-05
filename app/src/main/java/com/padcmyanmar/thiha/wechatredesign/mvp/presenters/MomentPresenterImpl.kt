package com.padcmyanmar.thiha.wechatredesign.mvp.presenters

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.padcmyanmar.thiha.wechatredesign.data.models.WeChatRedesignModelImpl
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import com.padcmyanmar.thiha.wechatredesign.mvp.views.MomentView
import com.padcmyanmar.thiha.wechatredesign.network.CloudFirestoreFirebaseApiImpl
import com.padcmyanmar.thiha.wechatredesign.network.FirebaseApi

class MomentPresenterImpl : ViewModel(), MomentPresenter {
    private var mView: MomentView? = null
    private val mMomentModel = WeChatRedesignModelImpl
      private var mId : String =""

    override fun initView(view: MomentView) {
        mView = view
    }

    override fun onTapCreateMoment() {
      mView?.navigateToCreateMoment()
    }

    override fun onUiReady(owner: LifecycleOwner) {

        mMomentModel.getUser(

            onSuccess = {
                mId = it.id.toString()
                mMomentModel.getMoment(
                    id = mId,

                    onSuccess = {


                        mView?.showPost(it)
                    },
                    onFailure = {

                        mView?.showError(it)
                    }
                )
            },
            onFailure = {

            }
        )




    }

    override fun onTapLike(
        momentMillis: String,
        likeCounts: Int,
        isLike: Boolean,
        onSuccess: () -> Unit
    ) {
     mMomentModel.giveLike(
         like = isLike,
         id = mId,
         likeCounts = likeCounts,
         momentMillis = momentMillis,
         onSuccess = {
             Log.d("reaction", "reaction success")
             onSuccess()
         },
         onFailure = {
             mView?.showError(it)
         })
    }

    override fun onTapBookmark(momentMillis: String, isBookmark: Boolean,onSuccess: () -> Unit) {
        mMomentModel.bookMarkMoment(
            BookMark = isBookmark,
            momentMillis = momentMillis,
            id = mId,
            onSuccess = {
                Log.d("bookmark", "bookmark success")
                onSuccess()
            },
            onFailure = {
                mView?.showError(it)
            }

        )

    }

    override fun onTapLikePeople(momentMillis: String, onSuccess: () -> Unit) {
       mView?.navigateToLikePeople(
           millis = momentMillis

       )
    }
}