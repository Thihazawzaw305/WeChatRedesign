package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.ActiveChatViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ChatDelegate


class ActiveChatAdapter(val chatDelegate: ChatDelegate) : RecyclerView.Adapter<ActiveChatViewHolder>() {
    private var mDataList = listOf<ContactVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_active_chat, parent,false)
        return ActiveChatViewHolder(view, chatDelegate)
    }

    override fun onBindViewHolder(holder: ActiveChatViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<ContactVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}