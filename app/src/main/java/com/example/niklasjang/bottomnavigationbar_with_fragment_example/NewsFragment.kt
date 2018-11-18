package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.ImageModel
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.SlidingImage_Adapter
import com.viewpagerindicator.CirclePageIndicator

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask


class NewsFragment : Fragment() {

    private class ViewHolder(row: Activity?) {

        var ibtn_1: ImageButton? =row?.findViewById(R.id.ibtn_1)
        var ibtn_2: ImageButton? = row?.findViewById(R.id.ibtn_2)
        var ibtn_3: ImageButton? = row?.findViewById(R.id.ibtn_3)
        var ibtn_4: ImageButton? = row?.findViewById(R.id.ibtn_4)
        var ibtn_5: ImageButton? = row?.findViewById(R.id.ibtn_5)
        var ibtn_6: ImageButton? = row?.findViewById(R.id.ibtn_6)
        init {
            ibtn_1?.setOnClickListener{
                Log.d("print","${ibtn_1?.toString()}")
            }
            ibtn_2?.setOnClickListener{
                Log.d("print","${ibtn_2?.toString()}")
            }
            ibtn_3?.setOnClickListener{
                Log.d("print","${ibtn_3?.toString()}")
            }
            ibtn_4?.setOnClickListener{
                Log.d("print","${ibtn_4?.toString()}")
            }
            ibtn_5?.setOnClickListener{
                Log.d("print","${ibtn_5?.toString()}")
            }
            ibtn_6?.setOnClickListener{
                Log.d("print","${ibtn_6?.toString()}")
            }

        }
    }
    private var imageModelArrayList: ArrayList<ImageModel>? = null
    private val myImageList = intArrayOf(R.drawable.harley2, R.drawable.benz2, R.drawable.vecto, R.drawable.webshots, R.drawable.bikess)
    var newsView :View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        newsView = inflater.inflate(R.layout.fragment_news, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        ViewHolder(activity)
    }



    private fun populateList(): ArrayList<ImageModel> {
        val list = ArrayList<ImageModel>()
        for (i in 0..3) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
        }
        return list
    }

    fun init() {
        imageModelArrayList = populateList()
        mPager = newsView?.findViewById(R.id.pager)
        mPager?.adapter = SlidingImage_Adapter(activity as Context, imageModelArrayList!!)

        val indicator = activity?.findViewById<CirclePageIndicator>(R.id.indicator)

        indicator?.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator?.setRadius(5 * density)
        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager?.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 5000)

        // Pager listener over indicator
        indicator?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position


            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })



    }

    companion object {
        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }


}
