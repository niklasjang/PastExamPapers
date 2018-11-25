package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MakePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_post)

        supportActionBar?.title = "Make Post"
    }
}
