package com.padcmyanmar.thiha.wechatredesign.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.ViewHolder.MomentViewHolder
import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO
import com.padcmyanmar.thiha.wechatredesign.delegates.MomentDelegate

class MomentRecyclerViewAdapter(val delegate : MomentDelegate) : RecyclerView.Adapter<MomentViewHolder>() {
    private var mMomentList: List<MomentVO> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_moment, parent, false)
        return MomentViewHolder(view,delegate)
    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        if(mMomentList.isNotEmpty()){
            holder.bind(mMomentList[position])
        }

    }

    override fun getItemCount(): Int {
       return mMomentList.size

    }

    fun setNewData(momentList: List<MomentVO>){
        mMomentList = momentList
        notifyDataSetChanged()
    }

}