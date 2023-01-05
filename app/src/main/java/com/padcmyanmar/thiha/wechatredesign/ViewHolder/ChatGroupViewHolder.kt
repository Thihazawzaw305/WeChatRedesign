package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padcmyanmar.thiha.wechatredesign.data.vos.GroupVO
import com.padcmyanmar.thiha.wechatredesign.delegates.GroupDelegate
import kotlinx.android.synthetic.main.view_holder_chat_group.view.*

class ChatGroupViewHolder(itemView: View, delegate: GroupDelegate):RecyclerView.ViewHolder(itemView) {

    var mGroupVO : GroupVO? = null

    init {
        itemView.setOnClickListener {
            mGroupVO?.let {
                delegate.onTapGroup(it)
            }
        }
    }


    fun bind(groupVO: GroupVO){
        mGroupVO = groupVO

        Glide.with(itemView.context)
            .load(groupVO.photo)
            .into(itemView.ivGroupPhoto)

        itemView.tvGroupName.text = groupVO.name

    }

}