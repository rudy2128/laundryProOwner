package com.anthony.laundrypro.ui.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Slider
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val imageUrl:ArrayList<String>):
SliderViewAdapter<SliderAdapter.SliderViewHolder>(){


    class SliderViewHolder(itemView: View?) : ViewHolder(itemView) {
        var imageView: ImageView = itemView!!.findViewById(R.id.image_slider)
    }

    override fun getCount(): Int {
        return imageUrl.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val inflate: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_slider, parent,false)
        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        if (viewHolder!=null){
            Glide.with(viewHolder.itemView.context)
                .load(imageUrl[position]).fitCenter()
                .override(300,126)
                .into(viewHolder.imageView)

        }
    }
}