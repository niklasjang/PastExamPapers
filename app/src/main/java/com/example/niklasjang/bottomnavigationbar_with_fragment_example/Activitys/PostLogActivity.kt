package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.graphics.PostProcessor
import android.opengl.GLES20
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import okhttp3.internal.http2.Http2

class PostLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_log)
        //TODO SET MAX WIDth in xml
//        android:MaxWidth
        var Button_Vote=findViewById<Button>(R.id.buttonVoting)
        Button_Vote.isEnabled = true

        val post = intent.getParcelableExtra<Post>(TimelineFragment.POST_KEY)
        supportActionBar?.title = post.uid
        val ref = FirebaseDatabase.getInstance().getReference("posts/${post.postname}/Vote_User_id")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(h in p0.children){
                        val value = h.value.toString()
                        if (value.equals(Id.toString())) {
                            Button_Vote.isEnabled = false

                        }
                    }
                }
            }
        })

        Button_Vote.setOnClickListener(){

            ref.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {


                    if (p0.exists()) {
                        for (h in p0.children) {
                            val value = h.value.toString()
                            if (value.equals(Id.toString())) {

                                Button_Vote.isEnabled = false
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
}
private  fun Process_Vote(){ //개발자에게 만 주어지는 소스, 즉시 처리(Voting)

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
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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