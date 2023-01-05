package com.padcmyanmar.thiha.wechatredesign.ViewHolder


import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.util.Log
import android.view.View

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.adapter.MomentImageRecyclerViewAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO
import com.padcmyanmar.thiha.wechatredesign.delegates.MomentDelegate
import kotlinx.android.synthetic.main.view_holder_moment.view.*
import kotlin.random.Random


class MomentViewHolder(itemView: View,delegate: MomentDelegate) : RecyclerView.ViewHolder(itemView) {

    var mMomentImageAdapter = MomentImageRecyclerViewAdapter()
    var mMoment: MomentVO? = null
    var ready: Boolean = false


    init {
        itemView.cbFav.setOnCheckedChangeListener { view, isChecked ->
            Log.d("first", isChecked.toString())
            mMoment?.let {
                if (ready)
                    delegate.onTapLike(it.millis.toString(), it.likeCounts, isChecked, onSuccess = {
                        updateLikeCount(isChecked, it.likeCounts)
                    })
            }

        }


        itemView.cbBookMark.setOnClickListener {
            mMoment?.let {
                delegate.onTapBookmark(it.millis.toString(), itemView.cbBookMark.isChecked, onSuccess = {

                })
            }
        }

//        itemView.tvFavCount.setOnClickListener {
//            mMoment?.let {
//                delegate.onTapLikePeople(it.millis.toString(), onSuccess = {
//
//                })
//            }
//        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(momentVO: MomentVO) {
        ready = false
        mMoment = momentVO
        itemView.tvUserNameFromMoment.text = momentVO.name

        itemView.tvCommentCount.text = (Random.nextInt(5) + 1).toString()

        Glide.with(itemView.context)
            .load(momentVO.profileImage)
            .into(itemView.ivUserProfile)

        itemView.tvMomentTime.text =
            DateUtils.getRelativeTimeSpanString(momentVO.millis as Long).toString()
        if (itemView.tvMomentTime.text == "0 minutes ago") {
            itemView.tvMomentTime.text = "just now"
        }
        itemView.tvMomentDescription.text = momentVO.description.toString()


        if(momentVO.likeCounts == 0){
            itemView.tvFavCount.text = ""

        }else{
            itemView.tvFavCount.text = momentVO.likeCounts.toString()
        }

        if (momentVO.isLike) {
            itemView.tvFavCount.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.red
                )
            )
        } else {
            itemView.tvFavCount.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.text_color_primary
                )
            )
        }


        itemView.cbFav.isChecked = momentVO.isLike
        itemView.cbBookMark.isChecked = momentVO.isBookmark
        ready = true


        if (momentVO.photoList.isNotEmpty()) {
            itemView.rvMomentPhoto.visibility = View.VISIBLE
            itemView.rvMomentPhoto.adapter = mMomentImageAdapter
            mMomentImageAdapter.setNewData(momentVO.photoList)
        } else {
            itemView.rvMomentPhoto.visibility = View.GONE
        }

        if (momentVO.videoLink.isNotEmpty()) {
            itemView.playerMoment.visibility = View.VISIBLE
            setUpPlayer(videoLink = momentVO.videoLink, itemView)
        } else {
            itemView.playerMoment.visibility = View.GONE
        }

    }

    private fun setUpPlayer(videoLink: String, itemView: View) {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(itemView.context)

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoLink))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        val simpleExoPlayer = ExoPlayer.Builder(itemView.context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        simpleExoPlayer.addMediaSource(mediaSource)

        simpleExoPlayer.playWhenReady = true


        itemView.playerMoment.player = simpleExoPlayer
        itemView.playerMoment.requestFocus()
    }

    private fun updateLikeCount(isIncrease: Boolean, totalCount: Int) {
        if (isIncrease) {
            mMoment!!.likeCounts++
            mMoment!!.isLike = true
            itemView.tvFavCount.text = (mMoment!!.likeCounts).toString()
            itemView.tvFavCount.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.red
                )
            )
        } else {
            mMoment!!.likeCounts--
            mMoment!!.isLike = false
            if (mMoment!!.likeCounts == 0) {
                itemView.tvFavCount.text = ""
            } else {
                itemView.tvFavCount.text = (mMoment!!.likeCounts).toString()
            }
            itemView.tvFavCount.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.text_color_primary
                )
            )

        }

    }

}
