package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.adapter.ContactAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactGroupVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactSelectDelegate
import kotlinx.android.synthetic.main.view_holder_contact_group.view.*

class ContactGroupViewHolder(itemView: View, val type: Int, val contactDelegate: ContactDelegate, val contactSelectDelegate: ContactSelectDelegate) : RecyclerView.ViewHolder(itemView) {
    lateinit var mContactAdapter: ContactAdapter

    fun bind(contactGroupVO: ContactGroupVO){
        itemView.tvContactGroupName.text = contactGroupVO.symbol
        setUpRecyclerView(contactGroupVO.contactList)
    }

    private fun setUpRecyclerView(contactList: List<ContactVO>){
        mContactAdapter = ContactAdapter(type, contactDelegate, contactSelectDelegate)
        itemView.rvContact.adapter = mContactAdapter
        mContactAdapter.setNewData(contactList)
    }
}