package com.akash.wardrobe.adapters

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.akash.wardrobe.R
import com.akash.wardrobe.database.Top
import com.bumptech.glide.Glide

class TopAdapter : RecyclerView.Adapter<TopViewHolder>() {
    private val TAG = TopAdapter::class.java.simpleName
    private var list: List<Top> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopViewHolder {
        return TopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.wardrobe_list_item, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TopViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    fun setData(list : List<Top>){
        this.list = list
        notifyDataSetChanged()
    }
}

class TopViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val TAG = TopViewHolder::class.java.simpleName
    private val topImage: ImageView = view.findViewById(R.id.imageView)
    fun bindItem(top: Top){
        Log.e(TAG,"top Uri: ${top.image}")
        Glide.with(topImage.context)
            .load(Uri.parse(top.image))
            .into(topImage)
    }
}