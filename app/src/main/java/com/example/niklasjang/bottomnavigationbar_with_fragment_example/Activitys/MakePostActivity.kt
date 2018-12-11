package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.Toast
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R.id.frameLayout_make_post
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

import android.os.Build.ID
import android.os.Parcelable
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.*
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment.Companion.USER_KEY
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.*
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R.id.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_make_post.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.post_row.view.*
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_post_log.*

class MakePostActivity : AppCompatActivity() {

    var Uri_file: String = ""
    val PDF: Int = 0
    lateinit var uri: Uri
    lateinit var mStorage: StorageReference

    //학과 spinner변수
    var major: String = ""
    var grade: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Make Post Activity에 reycler view 연습코드 적어둠.
        setContentView(R.layout.activity_make_post)
        supportActionBar?.title = "Make Post"
        var pdfBtn = findViewById<View>(R.id.btnPDF_make_post) as Button
        mStorage = FirebaseStorage.getInstance().getReference("Uploads")
        inflateAccordingToService()

        mk_progress.visibility = View.INVISIBLE


        pdfBtn.setOnClickListener(View.OnClickListener {
                view: View? -> val intent = Intent()
            intent.setType("*/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, PDF)
            Toast.makeText(this, "업로드할 파일을 선택하세요.", Toast.LENGTH_LONG).show()
        })
        spinner_grade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            //데이터가 변화하면 여기에 저장
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                grade = p0!!.getItemAtPosition(p2) as String
            }
        }
        spinner_major.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            //데이터가 변화하면 여기에 저장
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                major = p0!!.getItemAtPosition(p2) as String
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PDF) {
                uri = data!!.data
                Log.d("small uri_file", "Finally we saved the fileUri to Firebase Database : $uri ")
                upload()

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload() {
        val mReference = mStorage.child(uri.lastPathSegment)
        try {

            val filename = UUID.randomUUID().toString() //랜덤값 정의하기
            //"/images/ specify where image will be stored
            val pdfref = FirebaseStorage.getInstance().getReference("/Uploads/$filename")


            mk_progress.visibility = View.VISIBLE

            mReference.putFile(uri).addOnSuccessListener {
                //여기  taskSnapshot!!. function --> 고쳐야 제대로된 다운로드 URL이 저장이 됨.
                    taskSnapshot: UploadTask.TaskSnapshot ->
                var url = taskSnapshot!!.uploadSessionUri
//                val dwnTxt = findViewById<View>(R.id.dwnTxt) as TextView
//                dwnTxt.text = url.toString()
                mReference.downloadUrl.addOnSuccessListener {

                    Uri_file = it.toString()
                }
                Log.d("Uri_file", "Finally we saved the fileUri to Firebase Database : $Uri_file ")

                Toast.makeText(this, "업로드가 완료 되었습니다.", Toast.LENGTH_LONG).show()
                mk_progress.visibility = View.INVISIBLE

            }
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }


    private fun inflateAccordingToService() {
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
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val usersRef = FirebaseDatabase.getInstance().getReference("/users/$uid/username")
            usersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("onDataChanged2", "${p0.value}")
                    val author = p0.value.toString()
                    savePostToFirebaseDatabase(uid, author)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        }
    }

    private fun savePostToFirebaseDatabase(_uid: String, _author: String) {
        val lectureName = etLectureName_make_post.text.toString()
        val professorName = etProfessorName_make_post.text.toString()
        var title: String = ""
        if (lectureName.isEmpty() || professorName.isEmpty()){
            Toast.makeText(this, "텍스트를 모두 선택하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val year: Int?

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
        var contents: String = ""
        when (rgService.checkedRadioButtonId) {
            R.id.rbtn_QnA -> {
                service = 1
                contents = findViewById<EditText>(R.id.etContents_qna).text.toString()
                title = findViewById<EditText>(R.id.etTitle_qna).text.toString()

            }
            R.id.rbtn_Share -> {
                service = 2
                contents = findViewById<EditText>(R.id.etContents_share).text.toString()
                title = findViewById<EditText>(R.id.etTitle_share).text.toString()

            }
            else -> {
                service = null
            }
        }

        val author: String = ""
        val userUID = _uid
        val ref = FirebaseDatabase.getInstance().getReference("posts")
        val postname = ref.push().key ?: ""
        if(test == null || service == null){
            Toast.makeText(this, "버튼을 모두 선택하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        ref.child(postname).setValue(
            Post(
                Uri_file,
                postname,
                lectureName,
                professorName,
                title,
                grade,
                test,
                service,
                0.0,
                0,
                author,
                userUID,
                contents,
                0,
                1,
                major
            )
        )
            .addOnSuccessListener {
                Log.d("Register Activity", "Finally we saved the User to Firebase Database ")
                Toast.makeText(this, "Post upload success", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.d("Register Activity", "Badly we can't saved the User to Firebase Database : $it ")
                Toast.makeText(this, "Post upload Fail", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }

        Post() //게시물 올릴때 트랜젝션을 만들어 서버에 전송

        Fore_Check = 1

        Process_Post() // 즉시 처리(Voting)

        PostAcess(postname) // 게시물 원작자가  vote 하는 것을 막는 것, show 할 때  coin이 소모를 막는 것
        

    }



    private  fun PostAcess(postname :String){
        Vote_User_ref.child("${postname}")
            .child("Vote_User_id")
            .child("${UserId}")
            .setValue("$Id")

        Show_User_ref.child("${postname}")
            .child("Show_User_id")
            .child("${UserId}")
            .setValue("$Id")
    }

    private fun Post() { //게시물 올릴때 트랜젝션을 만들어 서버에 전송
        Coin += 30
        Post_Transaction_ref.child(Post_Transaction_ref.push().key!!).setValue(Id)

        val Hash = Show_ref.push().key
        val Info = ShowInfor2(id = Id.toString(), check = 0, hashID = Hash!!)
        Vote_ref.child(Hash!!).setValue(Info)

    }

    private fun Process_Post() { // 즉시 처리(Voting)

        Vote_ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
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
                for (h in Plus_List) {

                    for (h2 in Key_List) {
                        if (h.id == h2.id) {
                            if (Fore_Check == 0) {

                                return
                            }

                            val hero1 = Key(id = h2.id, uid = h2.uid, coin = h2.coin + 30, hashID = h2.hashID)
                            Key_List.set(h2.id.toInt() - 1, hero1)
                            Key_Save_ref.child(h2.hashID).setValue(hero1)

                            val hero2 = ShowInfor2(id = h.id, check = 1, hashID = h.hashID)
                            Vote_ref.child(h.hashID).setValue(hero2)

                        }
                    }
                }
                Fore_Check = 0
            }
        })
    }


}


