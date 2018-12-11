package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments


import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.User
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_register.*
import android.content.ContentResolver
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.*
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R.id.tvMyPost

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_my_accuont.*
import java.io.InputStream
import java.net.URL


//import sun.security.krb5.internal.KDCOptions.with





class MyAccuontFragment : Fragment() {
    //추가한 값들
    val ref = FirebaseDatabase.getInstance().getReference("/posts/")
    val adapter = GroupAdapter<ViewHolder>()
    //여기까지
    var recyclerView: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_accuont, container, false)
        val btnSettings = view.findViewById<Button>(R.id.btnSettings_my_account)

        val profileImage = view.findViewById<CircleImageView>(R.id.profile_image)


//        추가 텍스트 뷰
        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
//        추가 텍스트 뷰

        var url = ""
        //새로추가
        val email = FirebaseAuth.getInstance().currentUser
//        새로추가
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val urlRef = FirebaseDatabase.getInstance().getReference("users")
        val List : MutableList<User>
        List= mutableListOf()
        urlRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children){
                    val hero = h.getValue(User ::class.java)
                    List.add(hero!!)
                }
                for(h in List){
                    if(uid!!.equals(h.uid)){
                        println("TEST123 ${h.uid} ${h.profileImageUrl.toString()} ${h.username}")
                        val a=h.profileImageUrl
                        Picasso.get().load(a).into(profileImage)
                        Thread.sleep((3*1000).toLong())
                        tvUsername!!.text = "내 이름 : ${h.username}"
                        tvEmail!!.text = "내 계정 : ${email!!.email}"
                        tvCoinCnt!!.text = "보유 코인 수 : ${Coin.toString()}개"
                        tvMyPost!!.text = "<내 게시물>"

                    }
                }
                myacc_progress.visibility = View.INVISIBLE
//                url =p0.value.toString()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })



//        val background = object : Thread() {
//            override fun run() {
//                try {
//                    // Thread will sleep for 1 seconds
//                    Thread.sleep((3*1000).toLong())
//
//                    // After 5 seconds redirect to another intent
//                    //Remove activity
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//        // start thread
//        background.start()
//
//        myacc_progress.visibility = View.INVISIBLE


//        Picasso.get().load(url).into(profileImage)



        btnSettings.setOnClickListener {
            val intent = Intent(view.context, PreferenceActivity::class.java)
            startActivity(intent)
        }
        return view
    }
    //프레그먼트는 여기서 받아온다. 여기 아래까지는 추가한 것이다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_myAccount)
        recyclerView?.adapter = adapter
        fetchPost()
    }

    fun adapterNotifyDataSetChanged(){
        recyclerView?.adapter?.notifyDataSetChanged()
    }

    private fun fetchPost() {
        //If the addValueEventListener() method is used to add the listener,
        //the app will be notified every time the data changes in the specified subtree.
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val post = p0.getValue(Post::class.java)
                if (post != null) {
                    if(post.uid == FirebaseAuth.getInstance().currentUser!!.uid){
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