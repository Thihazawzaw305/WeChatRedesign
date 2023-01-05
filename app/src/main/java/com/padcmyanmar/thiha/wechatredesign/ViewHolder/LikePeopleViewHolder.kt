package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import kotlinx.android.synthetic.main.view_holder_like_people.view.*

class LikePeopleViewHolder(itemView : View):RecyclerView.ViewHolder(itemView) {

    fun bind(data : UserVO){

        itemView.tvUserNameFromLikePeople.text = data.name

        Glide.with(itemView.context)
            .load(data.profileUrl)
            .into(itemView.ivProfileImageFromLikes)

    }



}