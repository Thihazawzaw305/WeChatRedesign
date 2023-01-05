package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.MomentImageViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO


class MomentImageRecyclerViewAdapter: RecyclerView.Adapter<MomentImageViewHolder>() {
    private var mDataList = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_moment_image, parent, false)
        return MomentImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MomentImageViewHolder, position: Int) {
        if(mDataList.isNotEmpty()){
            holder.bind(mDataList[position])
        }

    }

    override fun getItemCount(): Int {
        return mDataList.size

    }

    fun setNewData(dataList: List<String>){
        mDataList = dataList
        notifyDataSetChanged()
    }

}