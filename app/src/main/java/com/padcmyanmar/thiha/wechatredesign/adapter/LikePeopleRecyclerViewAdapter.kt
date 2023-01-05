package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.LikePeopleViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO

class LikePeopleRecyclerViewAdapter  : RecyclerView.Adapter<LikePeopleViewHolder>() {
    private var mData: List<UserVO> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):LikePeopleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_like_people, parent, false)
        return LikePeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikePeopleViewHolder, position: Int) {
        if(mData.isNotEmpty()){
            holder.bind(mData[position])
        }

    }

    override fun getItemCount(): Int {
        return mData.size

    }

    fun setNewData(data: List<UserVO>){
        mData = data
        notifyDataSetChanged()
    }

}