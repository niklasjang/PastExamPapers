package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.User
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R

class PostLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_log)
        //TODO SET MAX WIDth in xml
//        android:MaxWidth

        val post = intent.getParcelableExtra<Post>(TimelineFragment.POST_KEY)
        supportActionBar?.title = post.uid
    }
}
