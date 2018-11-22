package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.accounts.NetworkErrorException
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.ArrayList
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_image.*

// 참고문서
// 1. https://stackoverflow.com/questions/13379194/how-to-add-a-fragment-inside-a-viewpager-using-nested-fragment-android-4-2

class NewsFragment : Fragment(){

    private var imageFragment :ImageFragment? = null
    private var NUM_PAGE = 3

    //뷰를 만들어서 return하고
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_news, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        var vpAdapter = MyAdapter(childFragmentManager, NUM_PAGE)
        viewPager.adapter = vpAdapter


    }



    //Inner class
    class MyAdapter(fm: FragmentManager, _pageCount : Int) : FragmentPagerAdapter(fm) {
        var pageCount :Int = _pageCount
        override fun getItem(p0: Int): Fragment {

            val args = Bundle()
            args.putInt("index", 0)

            val imageFragment = ImageFragment().newInstance(p0)
            //imageFragment.ivPhoto.setImageResource(imageModelArrayList[p0].getImage_drawables())
            return imageFragment
        }

        override fun getCount(): Int {
            return pageCount
        }
    }
}