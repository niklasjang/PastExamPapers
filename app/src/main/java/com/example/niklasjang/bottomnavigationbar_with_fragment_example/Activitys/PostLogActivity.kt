package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

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
import com.google.firebase.database.ValueEventListener

class PostLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_log)
        //TODO SET MAX WIDth in xml
//        android:MaxWidth
        var Button_Vote=findViewById<Button>(R.id.buttonVoting)

        val post = intent.getParcelableExtra<Post>(TimelineFragment.POST_KEY)
        supportActionBar?.title = post.uid

        Button_Vote.setOnClickListener(){
            Fore_Check=1
            Coin+=5
            val Hash=Show_ref.push().key
            val Info=ShowInfor2(id=Id.toString(),check=0,hashID =Hash!!)
            Vote_ref.child(Hash!!).setValue(Info)
            com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.Voteting(post)
            Process_Vote()
            Button_Vote.isClickable

        }
    }
}
private  fun Process_Vote(){ //개발자에게 만 주어지는 소스, 즉시 처리(Voting)

    Vote_ref.addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDataChange(p0: DataSnapshot) {
            for (h in p0.children) {
                val hero = h.getValue(ShowInfor2::class.java)
                Plus_List.clear()
                if (hero!!.check == 0) {

                    Plus_List.add(hero!!)
                }

            }
            println("TESTV1")
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
            println("TESTV2 ${Plus_List.size}")
            for(h in Plus_List){

                for(h2 in Key_List){
                    if(h.id == h2.id){
                        if(Fore_Check==0){

                            return
                        }

                        val hero1 = Key(id=h2.id, uid=h2.uid, coin=h2.coin +5, hashID = h2.hashID)
                        Key_List.set(h2.id.toInt()-1,hero1)
                        Key_Save_ref.child(h2.hashID).setValue(hero1)
                        val hero2= ShowInfor2(h.id,1,h.hashID)
                        Vote_ref.child(h.hashID).setValue(hero2)

                    }
                }
            }
            Fore_Check=0
        }
    })
}
private  fun Voteting(post :Post){ //vote 할 때 트랜젝션을 만들어 서버에 전송
    val name = post.postname
    val herold=Vote_Transaction_ref.push().key
    val vote: Vote =Vote(Id.toString())
    Vote_Transaction_ref.child("$name").child("$Id").setValue(vote)
}