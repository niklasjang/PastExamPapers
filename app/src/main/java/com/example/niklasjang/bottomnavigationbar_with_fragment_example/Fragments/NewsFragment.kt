package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments

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

// 참고문서
// 1. https://stackoverflow.com/questions/13379194/how-to-add-a-fragment-inside-a-viewpager-using-nested-fragment-android-4-2

class NewsFragment : Fragment() {

    private var imageFragment: ImageFragment? = null
    private var NUM_PAGE = 5 //프로젝트에 추가한 사진의 갯수만큼만

    //뷰를 만들어서 return하고
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        UserId = FirebaseAuth.getInstance().uid!!
        getKey()

        var view = inflater.inflate(R.layout.fragment_news, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        var vpAdapter =
            MyAdapter(
                childFragmentManager,
                NUM_PAGE
            )
        viewPager.adapter = vpAdapter
    }

    //Inner class
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
    private fun getKey() { //key 생성, 처음 login 했을 때
        var name: String


        Key_Save_ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    if(First_Login ==1){
                        return
                    }

                    Key_List.clear()

                    for (h in p0.children) {
                        val hero = h.getValue(Key::class.java)
                        Key_List.add(hero!!)
                    }

                    for (h in Key_List) {
                        if (h.uid.equals(UserId)) {

                            Id = h.id.toInt()
                            Coin =h.coin
                            HashID =h.hashID
                            First_Login = 1

                        }
                    }

                    if (First_Login != 1) {

                        val heroId = Key_Save_ref.push().key
                        val num = Key_List[Key_List.lastIndex].id.toInt()
                        val hero = Key((num + 1).toString(), UserId, 30,heroId!!)

                        Key_Save_ref.child(heroId!!).setValue(hero).addOnCompleteListener() {
                        }
                    }
                } else {
                    val heroId = Key_Save_ref.push().key
                    val hero = Key("1", UserId, 30,heroId!!)
                    Key_Save_ref.child(heroId!!).setValue(hero).addOnCompleteListener() {
                    }
                }
            }
        })

    }
}
