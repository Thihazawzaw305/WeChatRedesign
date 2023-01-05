package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import VIEW_TYPE_SELECT
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactDelegate
import com.padcmyanmar.thiha.wechatredesign.delegates.ContactSelectDelegate
import kotlinx.android.synthetic.main.view_holder_contact.view.*

class ContactViewHolder(itemView: View, val viewType: Int, contactDelegate: ContactDelegate, contactSelectDelegate: ContactSelectDelegate) : RecyclerView.ViewHolder(itemView) {

    var mContact : ContactVO? = null

    init {
        itemView.setOnClickListener {
            mContact?.let {
                contactDelegate.onTapContact(it)
            }
        }

        itemView.cbContact.setOnCheckedChangeListener { view, isCheck ->
            mContact?.let {
                contactSelectDelegate.onSelectContact(isCheck,it)
            }
        }
    }

    fun bind(contactVO: ContactVO) {
        mContact = contactVO

        itemView.tvContactName.text = contactVO.name

        Glide.with(itemView.context)
            .load(contactVO.photoUrl)
            .into(itemView.ivContactProfile)

        Log.d("viewType", viewType.toString())
        if (viewType == VIEW_TYPE_SELECT) {
            itemView.cbContact.visibility = View.VISIBLE
        } else {
            itemView.cbContact.visibility = View.GONE
        }
    }}