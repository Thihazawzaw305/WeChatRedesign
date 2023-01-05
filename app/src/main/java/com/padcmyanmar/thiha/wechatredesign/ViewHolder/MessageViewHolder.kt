package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.padcmyanmar.thiha.wechatredesign.adapter.MomentImageRecyclerViewAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.MessageVO
import kotlinx.android.synthetic.main.view_holder_other_message.view.*


class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mSmartImageAdapter = MomentImageRecyclerViewAdapter()

    fun bind(messageVO: MessageVO){
        // message
        if(messageVO.text.isNotEmpty()){
            itemView.tvMessageText.text = messageVO.text
            itemView.layoutMessageText.visibility = View.VISIBLE
        }else{
            itemView.layoutMessageText.visibility = View.GONE
        }

        // video
        if (messageVO.videoLink.isNotEmpty()) {
            itemView.playerMessage.visibility = View.VISIBLE
            setUpPlayer(videoLink = messageVO.videoLink, itemView)
        } else {
            itemView.playerMessage.visibility = View.GONE
        }

        // photo
        if (messageVO.photoList.isNotEmpty()) {
            itemView.rvMessagePhoto.visibility = View.VISIBLE
            itemView.rvMessagePhoto.adapter = mSmartImageAdapter
            mSmartImageAdapter.setNewData(messageVO.photoList)
            Log.d("photoListttt",messageVO.photoList.toString())
        } else {
            itemView.rvMessagePhoto.visibility = View.GONE
        }

        Glide.with(itemView.context)
            .load(messageVO.profileImage)
            .into(itemView.ivMessageProfile)

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
//        binding.playerView.player = simpleExoPlayer
//        binding.playerView.requestFocus()

        itemView.playerMessage.player = simpleExoPlayer
        itemView.playerMessage.requestFocus()
    }

}