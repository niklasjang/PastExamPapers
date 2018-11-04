package com.example.niklasjang.pastexampapers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.post_filter.*

class FilterActivity : AppCompatActivity() {

    val myFilter  by lazy { intent.extras["beforeFilter"] as Filter}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_filter)


        btnFirstYear.setOnClickListener { view->
            myFilter.fltYear = 1
        }

        btnSecondYear.setOnClickListener { view ->
            myFilter.fltYear = 2
        }

        btnThirdYear.setOnClickListener { view ->
            myFilter.fltYear = 3
        }
        btnFourthYear.setOnClickListener { view ->
            myFilter.fltYear = 4
        }
        btnFirstSemester.setOnClickListener { view->
            myFilter.fltSemester = 1
        }
        btnSecondSemester.setOnClickListener { view ->
            myFilter.fltSemester = 2
        }
        btnMidtermTest.setOnClickListener { view ->
            myFilter.fltTest = 1
        }
        btnFinalTest.setOnClickListener { view ->
            myFilter.fltTest = 2
        }
        btnBothTest.setOnClickListener { view->
            myFilter.fltTest = 3
        }

        btnSet.setOnClickListener { view ->
            var inputClassName = etClassName.text.toString()
            var inputProfessorName = etProfessorName.text.toString()
            myFilter.fltClassName = inputClassName
            myFilter.fltProfessorName = inputProfessorName
            Log.d("heregt", "${myFilter.fltClassName}")
            var resultIntent = Intent(this, TimelineActivity::class.java)
            resultIntent.putExtra("afterFilter", myFilter)
            setResult(101, resultIntent)
            finish()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        var inputClassName = etClassName.text.toString()
        var inputProfessorName = etProfessorName.text.toString()
        myFilter.fltClassName = inputClassName
        myFilter.fltProfessorName = inputProfessorName
        var resultIntent = Intent(this, TimelineActivity::class.java)
        resultIntent.putExtra("afterFilter", myFilter)
        setResult(101, resultIntent)
    }
}
