package com.akash.wardrobe.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.akash.wardrobe.R
import com.akash.wardrobe.database.Top


 /*This is sample class to demonstrate we can easily replace viewpager2 with normal viewpager
 and set this class as adapter*/

class ImagePagerAdapter(context: Context): PagerAdapter() {
    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    private var list: List<Top> = emptyList()

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val view = layoutInflater.inflate(R.layout.wardrobe_list_item,container,false)
        val imageView : ImageView = view.findViewById(R.id.imageView)

//        imageView.setImageURI(Uri.parse(list[position].image))
        container.addView(view)
        return view
    }

    override fun getCount(): Int = list.size

    fun setData(list: List<Top>){
        this.list = list
        notifyDataSetChanged()
    }

    /*override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeViewAt(position)
    }*/


}