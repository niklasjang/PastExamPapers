package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.sliding_images_layout.view.*


/**
 * Created by Parsania Hardik on 23/04/2016.
 */

class SlidingImage_Adapter(private val context: Context, private val imageModelArrayList: ArrayList<ImageModel>) : PagerAdapter() {

    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {


        val imageLayout = inflater.inflate(R.layout.sliding_images_layout, view, false)!!
        val imageView = imageLayout.findViewById(R.id.image) as ImageView
        imageView.setImageResource(imageModelArrayList[position].getImage_drawables())

        view.addView(imageLayout, 0)
        view.image.setOnClickListener {
            Log.d("print", "${position}")
        }




        return imageLayout

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
}


