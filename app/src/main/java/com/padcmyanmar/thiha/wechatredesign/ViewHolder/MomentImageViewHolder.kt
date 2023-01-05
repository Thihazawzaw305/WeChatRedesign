package com.padcmyanmar.thiha.wechatredesign.ViewHolder

import android.graphics.Bitmap
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_create_moment.*
import kotlinx.android.synthetic.main.view_holder_file.view.*
import kotlinx.android.synthetic.main.view_holder_moment_image.view.*

class MomentImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {


    fun bind(image: String){
//
//        Glide.with(itemView.context)
//            .load(image)
//            .into(itemView.ivMomentImage)


        Log.d("my_photo_list",image)

        Glide.with(itemView.context)
//            .load(image)
//            .into(itemView.ivMomentImage)
            .asBitmap()
            .load(image)
            .into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val width = resource.width
                    val height = resource.height

                    if(width > height){
                        val dimensionWidth = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            350F,
                            itemView.context.resources.displayMetrics
                        ).toInt()

                        val dimensionHeight = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            200F,
                            itemView.context.resources.displayMetrics
                        ).toInt()

                        itemView.ivMomentImage.layoutParams.width = dimensionWidth
                        itemView.ivMomentImage.layoutParams.height = dimensionHeight
                        itemView.ivMomentImage.requestLayout()
//                          itemView.ivSmartImage.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT - dimensionInDp
//                          itemView.ivSmartImage.requestLayout()
                    }else{
                        val dimensionWidth = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            200F,
                            itemView.context.resources.displayMetrics
                        ).toInt()

                        val dimensionHeight = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            270F,
                            itemView.context.resources.displayMetrics
                        ).toInt()
                        itemView.ivMomentImage.layoutParams.height = dimensionHeight
                        itemView.ivMomentImage.layoutParams.width = dimensionWidth
                        itemView.ivMomentImage.requestLayout()
                    }

                    itemView.ivMomentImage.setImageBitmap(resource)

                }
            })
   }

}