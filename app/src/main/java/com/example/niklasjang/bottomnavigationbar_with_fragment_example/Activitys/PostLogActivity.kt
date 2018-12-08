package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.graphics.PostProcessor
import android.opengl.GLES20
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.Key
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.ShowInfor2
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.User
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.Vote
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_post_log.*
import kotlinx.android.synthetic.main.activity_post_log.view.*
import okhttp3.internal.http2.Http2

class PostLogActivity : AppCompatActivity() {
    lateinit  var btnVote : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_log)
        //TODO SET MAX WIDth in xml
//        android:MaxWidth
        val post = intent.getParcelableExtra<Post>(TimelineFragment.POST_KEY)
        supportActionBar?.title = post.title

        val adapter = GroupAdapter<ViewHolder>()
        val recyclerView : RecyclerView? = findViewById(R.id.recyclerview_post_log)
        recyclerView?.adapter = adapter
//        adapter.add(0,PostEntryItem(post))
        adapter.notifyDataSetChanged()

//        val postEntry = findViewById<ConstraintLayout>(R.id.constraint_post_entry)
//        Log.d("PostLogActivity","recyclerview is ${recyclerView?.layoutManager}")
        tvReward_post_entry.text = "5.7"
        btnVote = findViewById(R.id.tvVote_post_entry)
        btnVote.isEnabled = true

        val ref = FirebaseDatabase.getInstance().getReference("posts/${post.postname}/Vote_User_id")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(h in p0.children){
                        val value = h.value.toString()
                        if (value.equals(Id.toString())) {
                            btnVote.isEnabled = false
                        }
                    }
                }
            }
        })
        btnVote.setOnClickListener{
            ref.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (h in p0.children) {
                            val value = h.value.toString()
                            if (value.equals(Id.toString())) {
                                btnVote.isEnabled = false
                            } else {
                                Vote_User_ref.child("${post.postname}")
                                    .child("Vote_User_id")
                                    .child("${UserId}")
                                    .setValue("$Id")

                                Second_Check = 1
                                Coin += 5

                                val Hash = Vote_ref.push().key
                                val Info = ShowInfor2(id = Id.toString(), check = 0, hashID = Hash!!)

                                Vote_ref.child(Hash!!).setValue(Info)
                                com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.Voteting(post)
                                Process_Vote()
                            }
                        }
                    }else{

                        Vote_User_ref.child("${post.postname}")
                            .child("Vote_User_id")
                            .child("${UserId}")
                            .setValue("$Id")

                        Coin += 5

                        Second_Check = 1

                        val Hash = Vote_ref.push().key
                        val Info = ShowInfor2(id = Id.toString(), check = 0, hashID = Hash!!)
                        Vote_ref.child(Hash!!).setValue(Info)

                        com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.Voteting(post)
                        Process_Vote()
                    }
                }
            })
            Five_Check=1
            Post_Vote(post)
        }
    }

    //상단 menu bar 생성하기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_log_top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //상단 menu bar select listener.
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_comment -> {
                //TODO 어떤 fragment에서 넘어온 건지 기억해서 돌아가기. 지금은 manifests에 parent actiyivty만 설정했음.
                // TODO 그래서 Main Activit가 처음 시작될 때 News Fragment가 시작되게 설정한 것이 자동으로 시작됨.
                val intent = Intent(this, MakeCommentActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

private  fun Process_Vote(){ //개발자에게 만 주어지는 소스, 즉시 처리(Voting)
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
                        if(Second_Check==0){
                            return
                        }else {
                            val hero1 = Key(id = h2.id, uid = h2.uid, coin = h2.coin + 5, hashID = h2.hashID)
                            Key_List.set(h2.id.toInt() - 1, hero1)
                            Key_Save_ref.child(h2.hashID).setValue(hero1)
                            val Info = ShowInfor2(id = Id.toString(), check = 1, hashID = h.hashID!!)
                            Vote_ref.child(h.hashID!!).setValue(Info)
                        }
                    }
                }
            }
            Second_Check=0
        }
    })
}

private  fun Post_Vote(post :Post){
    Vote_User_ref.addValueEventListener( object :ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun onDataChange(p0: DataSnapshot) {
            if(Five_Check==0){
                return
            }
            Key_Save_ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {

                    Key_List.clear()
                    for (h in p0.children) {
                        val hero = h.getValue(Key::class.java)
                        Key_List.add(hero!!)
                    }
                    for(h2 in Key_List){
                        if(post.Id == h2.id.toInt()){
                            if(Five_Check==0){
                                println("TEST 2234")
                                return
                            }else {
                                println("TEST 2233")
                                val hero1 = Key(id = h2.id, uid = h2.uid, coin = h2.coin + 2, hashID = h2.hashID)
                                Key_List.set(h2.id.toInt() - 1, hero1)
                                Key_Save_ref.child(h2.hashID).setValue(hero1)
                            }
                        }
                    }
                    Five_Check=0
                }
            })
        }
    })
}

private  fun Voteting(post :Post){ //vote 할 때 트랜젝션을 만들어 서버에 전송
    val name = post.postname
    val herold=Vote_Transaction_ref.push().key
    val vote: Vote =Vote(Id.toString(),0,herold!!)
    Vote_Transaction_ref.child("$name").child("$Id").setValue(vote)
}


////Item은  com.xwray.groupie에 정의된 타입으로  그냥 받아들이면 됨
//class PostEntryItem(val post: Post) : Item<ViewHolder>() {
//    //여기서 return한 layout 파일의 형식대로 recycler view에 추가됨.
//    override fun getLayout(): Int {
//        return R.layout.post_entry
//    }
//
//    override fun bind(viewHolder: ViewHolder, position: Int) {
//        //viewHolder.itemView까지 하면 view를 얻는다고 보면 됨.
//        viewHolder.itemView.tvLecturename_post_entry.text = "[${post.lecturename}]"
//        viewHolder.itemView.tvProfessorname_post_entry.text = "${post.professorName}교수님"
//        viewHolder.itemView.tvService_post_entry.text = "${post.service}"
//        viewHolder.itemView.tvYear_post_entry.text = "${post.year}학년"
//        viewHolder.itemView.tvTest_post_entry.text = "[${post.test}시험"
//        viewHolder.itemView.tvTitle_post_entry.text = "[${post.title}]"
//        viewHolder.itemView.tvComment_post_entry.text = "[${post.contents}]"
//        viewHolder.itemView.tvUsername_post_entry.text = "[${post.author}]"
//        viewHolder.itemView.tvReward_post_entry.text = "[${post.reward}]"
//        viewHolder.itemView.tvVote_post_entry.text = "[${post.vote}]"
////        viewHolder.itemView.tvDate_post_entry.text = "[${post.data}]"
//        //get CommentItemCount
////        viewHolder.itemView.tvComment_post_entry.text = "[${post.c}]"
//
//
//        //TODO 사진 업로드. 프로필 이미지 업로드 이렇게 하면 됨.
//
////        post.uid
//        //Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.ivPostImage)
//    }
//
//}