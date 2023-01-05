package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ChatDelegate
import kotlinx.android.synthetic.main.view_holder_active_chat.view.*
import kotlinx.android.synthetic.main.view_holder_active_user.view.*

class ActiveChatViewHolder(itemView: View,chatDelegate: ChatDelegate) : RecyclerView.ViewHolder(itemView) {
    var mContactVO : ContactVO? = null
    init {
        itemView.setOnClickListener {
            mContactVO?.let { contact ->
                chatDelegate.onTapChat(contact)
            }
        }
    }


    fun bind(contactVO: ContactVO){
        mContactVO = contactVO

        itemView.tvActiveChatName.text = contactVO.name

        Glide.with(itemView.context)
            .load(contactVO.photoUrl)
            .into(itemView.ivActiveChatProfile)

        itemView.tvActiveChatLastText.text = contactVO.lastMessage
    }
}