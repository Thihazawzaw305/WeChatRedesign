package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.FileViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.delegates.FileDelegate

class FileAdapter(private val fileDelegate: FileDelegate): RecyclerView.Adapter<FileViewHolder>() {
    private var mDataList = listOf<FileVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_file, parent,false)
        return FileViewHolder(view,fileDelegate)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<FileVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}
