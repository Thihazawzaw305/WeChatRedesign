package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.ContentViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContentVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ContentDelegate


class ContentAdapter(val contentDelegate: ContentDelegate) : RecyclerView.Adapter<ContentViewHolder>() {
    var mDataList = listOf<ContentVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_content, parent,false)
        return ContentViewHolder(view,contentDelegate)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<ContentVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}