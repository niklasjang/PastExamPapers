package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        val background = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 1 seconds
                    Thread.sleep((3*1000).toLong())
                    // After 5 seconds redirect to another intent
                    val i = Intent(baseContext, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    //Remove activity
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        // start thread
        background.start()
    }

}
