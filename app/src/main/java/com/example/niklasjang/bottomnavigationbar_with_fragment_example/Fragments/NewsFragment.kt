package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments

import android.content.Intent
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.*
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.Key
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_news.*

// 참고문서
// 1. https://stackoverflow.com/questions/13379194/how-to-add-a-fragment-inside-a-viewpager-using-nested-fragment-android-4-2

class NewsFragment : Fragment() {

    private var imageFragment: ImageFragment? = null
    private var NUM_PAGE = 3 //프로젝트에 추가한 사진의 갯수만큼만

    //뷰를 만들어서 return하고
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        var view = inflater.inflate(R.layout.fragment_news, container, false)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        val vpAdapter =
            MyAdapter(
                childFragmentManager,
                NUM_PAGE
            )
        viewPager.adapter = vpAdapter
        iv_newsFragmentSoft.setOnClickListener {
            var intent = Intent(activity,MajorTimeLineActivity::class.java)
            intent.putExtra("major","소프트웨어")
            startActivity(intent)
        }
        iv_newsFragmentAero.setOnClickListener {
            var intent = Intent(activity,MajorTimeLineActivity::class.java)
            intent.putExtra("major","운항")
            startActivity(intent)

        }
        iv_newsFragmentManage.setOnClickListener {
            var intent = Intent(activity,MajorTimeLineActivity::class.java)
            intent.putExtra("major","경영")
            startActivity(intent)

        }
        iv_newsFragmentElec.setOnClickListener {
            var intent = Intent(activity,MajorTimeLineActivity::class.java)
            intent.putExtra("major","항전정")
            startActivity(intent)

        }
        iv_newsFragmentMaterial.setOnClickListener {
            var intent = Intent(activity,MajorTimeLineActivity::class.java)
            intent.putExtra("major","재료")
            startActivity(intent)

        }
        iv_newsFragmentMechanic.setOnClickListener {
            var intent = Intent(activity,MajorTimeLineActivity::class.java)
            intent.putExtra("major","항우기")
            startActivity(intent)

        }

    }

    //Inner classo
    class MyAdapter(fm: FragmentManager, _pageCount: Int) : FragmentPagerAdapter(fm) {
        var pageCount: Int = _pageCount
        override fun getItem(p0: Int): Fragment {

            val args = Bundle()
            args.putInt("index", 0)
            val imageFragment = ImageFragment()
                .newInstance(p0)
            return imageFragment
        }

        override fun getCount(): Int {
            return pageCount
        }
    }

}
