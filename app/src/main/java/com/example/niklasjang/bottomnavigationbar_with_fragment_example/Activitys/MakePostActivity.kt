package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.os.Build.ID
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.*
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment.Companion.USER_KEY
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.*
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

        val ref = FirebaseDatabase.getInstance().getReference("posts")
        val postname=ref.push().key
        ref.child(postname!!).setValue(
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
                contents,
                Id
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

        Post()
        Fore_Check = 1
        Process_Post()
        Vote_User_ref.child("${postname}")
            .child("Vote_User_id")
            .child("${UserId}")
            .setValue("$Id")

        Show_User_ref.child("${postname}")
            .child("Show_User_id")
            .child("${UserId}")
            .setValue("$Id")

    }
    private  fun Post(){ //게시물 올릴때 트랜젝션을 만들어 서버에 전송
        Coin+=30
        Post_Transaction_ref.child(Post_Transaction_ref.push().key!!).setValue(Id)

        val Hash = Show_ref.push().key
        val Info = ShowInfor2(id = Id.toString(), check = 0, hashID = Hash!!)
        Vote_ref.child(Hash!!).setValue(Info)

    }
    private  fun Process_Post(){ //개발자에게 만 주어지는 소스, 즉시 처리(Voting)

        Vote_ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                Plus_List.clear()
                for (h in p0.children) {
                    val hero = h.getValue(ShowInfor2::class.java)

                    if (hero!!.check == 0) {

                        Plus_List.add(hero!!)

                    }

                }

                Key_Save_ref.addValueEventListener(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {



                        Key_List.clear()
                        for (h in p0.children) {
                            val hero = h.getValue(Key::class.java)
                            Key_List.add(hero!!)

                        }
                    }
                })
                for(h in Plus_List){

                    for(h2 in Key_List){
                        if(h.id == h2.id){
                            if(Fore_Check==0){

                                return
                            }

                            val hero1 = Key(id=h2.id, uid=h2.uid, coin=h2.coin +30, hashID = h2.hashID)
                            Key_List.set(h2.id.toInt()-1,hero1)
                            Key_Save_ref.child(h2.hashID).setValue(hero1)

                            val hero2= ShowInfor2(id = h.id, check = 1, hashID = h.hashID)
                            Vote_ref.child(h.hashID).setValue(hero2)

                        }
                    }
                }
                Fore_Check=0
            }
        })
    }

}


