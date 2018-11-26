package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.FilterActivity
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.PostAdapter
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R


class TimelineFragment : Fragment() {
    var lvPostlist : ListView? = null
    var btnFilter : Button? = null
    val posts = arrayListOf(
        Post(
            "항공제어sw",
            "최영식",
            2,
            1,
            1,
            author = "장환석"
        ),
        Post(
            "알고리즘",
            "이인복",
            2,
            2,
            1,
            author = "이인복",
            scrap = true
        ),
        Post(),
        Post(),
        Post(),
        Post(),
        Post(),
        Post(),
        Post()
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == 100 && resultCode == Activity.RESULT_OK && data != null){
            //TODO
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFilter = view.findViewById<Button>(R.id.btnFilter)

        btnFilter?.setOnClickListener {
            var intent = Intent(activity, FilterActivity::class.java)
            startActivityForResult(intent,100 )
        }

        lvPostlist = view.findViewById(R.id.lvPostList)
        val postAdapter =
            PostAdapter(activity!!, posts)
        lvPostlist?.adapter = postAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }


}
