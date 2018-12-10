package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.UserItem
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class MajorTimeLineActivity : AppCompatActivity() {
    var filterArray = arrayListOf<Post>()
    var valueArray = arrayListOf<Post>()
    val adapter = GroupAdapter<ViewHolder>()
    var recyclerView: RecyclerView? = null
    val major by lazy { intent.extras["major"] as String }
    var changedMajor : String = ""
    val ref = FirebaseDatabase.getInstance().getReference("/posts/")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major_time_line)
        recyclerView = findViewById<RecyclerView>(R.id.rv_majorTimeline)
        recyclerView?.adapter = adapter
        supportActionBar?.title = "전공별 게시판"
        println("major is" + major)
        fetchPost(major)
    }

    fun adapterNotifyDataSetChanged(){
        recyclerView?.adapter?.notifyDataSetChanged()
    }

    private fun fetchPost(major : String) {
        //If the addValueEventListener() method is used to add the listener,
        //the app will be notified every time the data changes in the specified subtree.
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val post = p0.getValue(Post::class.java)
                if (post != null) {
                    if(major == "소프트"){
                        adapter.add(0, UserItem(post))
                    }
                    //savePostToFirebaseDatabase에서 setValue한 형식대로 get을 한다.
//                    adapter.add(adapter.itemCount, UserItem(post))
                }
                adapterNotifyDataSetChanged()
                //각 post들을 클릭했을 때 나오는 화면
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, PostLogActivity::class.java)
                    intent.putExtra(TimelineFragment.POST_KEY, userItem.post)
                    startActivity(intent)
                }
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}
