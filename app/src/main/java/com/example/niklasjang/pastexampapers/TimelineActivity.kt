package com.example.niklasjang.pastexampapers

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_timeline.*
import android.R.attr.data
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationCompat.getExtras




class TimelineActivity : AppCompatActivity() {

    var lvPostlist : ListView? = null
    var btnFilter : Button? = null
    val posts = arrayListOf(
            Post("항공제어sw", "최영식", 2, 1, 1, author = "장환석" ),
            Post("알고리즘", "이인복",2,2,1, author ="이인복",scrap = true),
            Post(),
            Post(),
            Post(),
            Post(),
            Post(),
            Post(),
            Post()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        btnFilter = findViewById(R.id.btnFilter)
        lvPostlist = findViewById(R.id.lvPostList)
        val postAdapter = PostAdapter(this,posts)
        lvPostlist?.adapter = postAdapter

        btnFilter?.setOnClickListener { view->
            var filterIntent = Intent(this, FilterActivity::class.java )
            var myFilter = Filter()
            filterIntent.putExtra("beforeFilter",myFilter)
            this.startActivityForResult(filterIntent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val bundle = data?.getExtras()
        if (bundle != null) {
            for (key in bundle.keySet()) {
                val value = bundle.get(key)
                Log.d("tagtag", String.format("%s %s (%s)", key,
                        value!!.toString(), value.javaClass.name))
            }
        }

        if(data != null){
            if(requestCode == 100 && resultCode == 101){ //from FilterActivity
               var afterFilter =  data.extras["afterFilter"] as Filter
                Log.d("casted1", "${afterFilter.fltClassName}")
                afterFilter = afterFilter as Filter
                Log.d("casted2", "${afterFilter.fltClassName}")

                Toast.makeText(applicationContext,afterFilter.toString(), Toast.LENGTH_LONG).show()

            }
        }

    }

}
