package com.akash.wardrobe.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.akash.wardrobe.R
import com.akash.wardrobe.database.Bottom
import com.bumptech.glide.Glide

class BottomAdapter : RecyclerView.Adapter<BottomViewHolder>() {
    private var list: List<Bottom> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomViewHolder {
        return BottomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.wardrobe_list_item, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    fun setData(list : List<Bottom>){
        this.list = list
        notifyDataSetChanged()
    }
}

class BottomViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val TAG = BottomViewHolder::class.java.simpleName
    private val bottomImage: ImageView = view.findViewById(R.id.imageView)
    fun bindItem(bottom: Bottom){
        Log.e(TAG,"bottom Uri: ${bottom.image}")
        Glide.with(bottomImage.context)
            .load(Uri.parse(bottom.image))
            .into(bottomImage)
    }
}