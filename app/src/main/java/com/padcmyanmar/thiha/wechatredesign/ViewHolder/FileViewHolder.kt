package com.padcmyanmar.thiha.wechatredesign.ViewHolder


import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.delegates.FileDelegate
import com.padcmyanmar.thiha.wechatredesign.mvp.views.CreateMomentView
import kotlinx.android.synthetic.main.view_holder_file.view.*


class FileViewHolder(itemView: View, delegate: FileDelegate) : RecyclerView.ViewHolder(itemView) {
    var mFileVO: FileVO? = null

    init {
        itemView.ivFileRemove.setOnClickListener {

            mFileVO?.let {
                mFileVO = it
                delegate.onTapDelete(it)

            }
        }
    }

    fun bind(fileVO: FileVO) {
        mFileVO = fileVO

        if (fileVO.isMovie) {
            itemView.ivPlayArrow.visibility = View.VISIBLE
        } else {
            itemView.ivPlayArrow.visibility = View.GONE
        }
        Glide.with(itemView.context)
            .load(fileVO.bitmap)
            .into(itemView.ivFileImage)
    }
}


//        if(fileVO.isMovie){
//            itemView.ivPlayArrow.visibility = View.VISIBLE
//        }else{
//            itemView.ivPlayArrow.visibility = View.GONE
//        }
//        Glide.with(itemView.context)
//            .load(fileVO.bitmap)
//            .into(itemView.ivFileImage)