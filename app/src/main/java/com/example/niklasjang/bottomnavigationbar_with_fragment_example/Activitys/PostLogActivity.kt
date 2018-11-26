package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R

class PostLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_log)
        //TODO SET MAX WIDth in xml
//        android:MaxWidth

        val user =  intent.getParcelableExtra<User>(MakePostActivity.USER_KEY)
        supportActionBar?.title=user.username
    }
}
