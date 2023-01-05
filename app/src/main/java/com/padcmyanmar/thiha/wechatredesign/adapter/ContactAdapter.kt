package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.ContactViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactSelectDelegate


class ContactAdapter(val type: Int, val contactDelegate: ContactDelegate, val contactSelectDelegate: ContactSelectDelegate) : RecyclerView.Adapter<ContactViewHolder>() {
    var mDataList = listOf<ContactVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_contact, parent,false)
        return ContactViewHolder(view, type ,contactDelegate, contactSelectDelegate)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
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