package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.ContactGroupViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactGroupVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactSelectDelegate


class ContactGroupAdapter(val type: Int, val contactDelegate: ContactDelegate, val contactSelectDelegate: ContactSelectDelegate) : RecyclerView.Adapter<ContactGroupViewHolder>() {
    var mDataList = listOf<ContactGroupVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_contact_group, parent,false)
        return ContactGroupViewHolder(view, type, contactDelegate, contactSelectDelegate)
    }

    override fun onBindViewHolder(holder: ContactGroupViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<ContactGroupVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}