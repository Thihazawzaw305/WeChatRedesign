package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContentVO
import com.padcmyanmar.thiha.wechatredesign.delegates.ContentDelegate
import kotlinx.android.synthetic.main.view_holder_content.view.*

class ContentViewHolder(itemView: View, contentDelegate: ContentDelegate) :
    RecyclerView.ViewHolder(itemView) {
    var mContent: ContentVO? = null

    init {
        itemView.setOnClickListener {
            mContent?.let {
                contentDelegate.onTapContent(it.content)

            }
        }
    }

    fun bind(contentVO: ContentVO) {
        mContent = contentVO

        itemView.ivContent.setImageDrawable(ContextCompat.getDrawable(itemView.context,contentVO.image))

        if(contentVO.isSelected){
            itemView.cardContent.background.setTint(ContextCompat.getColor(itemView.context, R.color.text_color_primary))
//            itemView.cardContent.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primary_color))
            itemView.ivContent.setColorFilter(ContextCompat.getColor(itemView.context, R.color.white))
        }else{
            itemView.cardContent.background.setTint(ContextCompat.getColor(itemView.context, R.color.white))
//            itemView.cardContent.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.based_white))
            itemView.ivContent.setColorFilter(ContextCompat.getColor(itemView.context, R.color.text_color_primary))

        }
    }
}