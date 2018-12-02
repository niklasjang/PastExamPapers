package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.*
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment.Companion.USER_KEY
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.User
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R.id.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_make_post.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.post_row.view.*
import java.util.*

class MakePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Make Post Activity에 reycler view 연습코드 적어둠.
        setContentView(R.layout.activity_make_post)
        supportActionBar?.title = "Make Post"
        inflateAccordingToService()

    }

    private fun inflateAccordingToService(){
        val rgService = findViewById<RadioGroup>(R.id.rgService)
        val framelayout = findViewById<FrameLayout>(R.id.frameLayout_make_post)
        rgService.setOnCheckedChangeListener { group, checkedId -> }
        rbtn_QnA.setOnClickListener {

            if (framelayout.childCount > 0) {
                framelayout.removeViewAt(0)
            }
            layoutInflater.inflate(R.layout.question_and_answer, frameLayout_make_post, true)
        }

        rbtn_Share.setOnClickListener {
            if (framelayout.childCount > 0) {
                framelayout.removeViewAt(0)
            }
            layoutInflater.inflate(R.layout.knowledge_share, frameLayout_make_post, true)

        }
        //TODO file upload

        //TODO btnDone 버튼 통합?
        val btnDone = findViewById<Button>(R.id.btnDone_make_post)
        btnDone.setOnClickListener {
            savePostToFirebaseDatabase()
        }
    }
    private fun savePostToFirebaseDatabase() {
        val lectureName = etLectureName_make_post.text.toString()
        val professorName = etProfessorName_make_post.text.toString()

        if(lectureName.isEmpty() || professorName.isEmpty()) return

        val year: Int?
        when (rgYear_make_post.checkedRadioButtonId) {
            R.id.rbtn_1year -> {
                year = 1
            }
            R.id.rbtn_2year -> {
                year = 2
            }
            R.id.rbtn_3year -> {
                year = 3
            }
            R.id.rbtn_4year -> {
                year = 4
            }
            R.id.rbtn_year_all -> {
                year = 0
            }
            else -> {
                year = null
            }
        }
        val test: Int?
        when (rgTest.checkedRadioButtonId) {
            R.id.rbtn_midterm -> {
                test = 1
            }
            R.id.rbtn_final -> {
                test = 2
            }
            R.id.rbtn_test_all -> {
                test = 0
            }
            else -> {
                test = null
            }
        }
        val service: Int?
        var contents :String=""
        when (rgService.checkedRadioButtonId) {
            R.id.rbtn_QnA -> {
                service = 1
                contents = findViewById<EditText>(R.id.etQnA_qna).text.toString()
            }
            R.id.rbtn_Share -> {
                service = 2
                contents = findViewById<EditText>(R.id.etShare_share).text.toString()
            }
            else -> {
                service = null
            }
        }
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val postname= UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().getReference("/posts/$postname")
        ref.setValue(
            Post(
                postname,
                lectureName,
                professorName,
                year!!,
                test!!,
                service!!,
                0.0,
                0,
                uid,
                contents
                )
        )
            .addOnSuccessListener {
                Log.d("Register Activity", "Finally we saved the User to Firebase Database ")
                Toast.makeText(this,"Post upload success",Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.d("Register Activity", "Badly we can't saved the User to Firebase Database : $it ")
                Toast.makeText(this,"Post upload Fail",Toast.LENGTH_SHORT).show()
                Toast.makeText(this,"$it",Toast.LENGTH_LONG).show()
            }
    }


}


