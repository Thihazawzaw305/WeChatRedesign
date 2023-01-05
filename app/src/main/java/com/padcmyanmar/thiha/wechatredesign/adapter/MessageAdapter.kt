package com.padcmyanmar.thiha.wechatredesign.adapter

import VIEW_TYPE_OTHER_MESSAGE
import VIEW_TYPE_OWN_MESSAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.MessageViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.MessageVO



class MessageAdapter() : RecyclerView.Adapter<MessageViewHolder>() {
    var mDataList = listOf<MessageVO>()
    var ownId: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_own_message, parent,false)

        var view: View? = null
        if(viewType == VIEW_TYPE_OWN_MESSAGE){
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_own_message, parent,false)
        }else if(viewType == VIEW_TYPE_OTHER_MESSAGE){
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_other_message, parent,false)
        }

        return MessageViewHolder(view!!)

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<MessageVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }

    fun setId(id: String){
        ownId = id
    }

    override fun getItemViewType(position: Int): Int {
        if(ownId == mDataList[position].id.trim()){
             return VIEW_TYPE_OWN_MESSAGE
         }else{
             return VIEW_TYPE_OTHER_MESSAGE
         }
    }
}