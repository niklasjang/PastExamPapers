package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.FilterActivity
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.PostLogActivity
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.post_row.view.*
import android.view.animation.LinearInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.google.firebase.database.*


class TimelineFragment : Fragment() {
    val adapter = GroupAdapter<ViewHolder>()
    var recyclerView: RecyclerView? = null
    val ref = FirebaseDatabase.getInstance().getReference("/posts/")

    companion object {
        val USER_KEY = "USER_KEY"
        val POST_KEY = "POST_KEY"
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_timeline)
        recyclerView?.adapter = adapter
        fetchPost()
        val btnFilter = view.findViewById<Button>(R.id.btnFilter)
        btnFilter?.setOnClickListener {
            val intent = Intent(activity, FilterActivity::class.java)
            startActivityForResult(intent, 100)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            //TODO 필터 구현
        }
    }


    fun adapterNotifyDataSetChanged(){
        recyclerView?.adapter?.notifyDataSetChanged()
    }
    private fun fetchPost() {
        //If the addValueEventListener() method is used to add the listener,
        //the app will be notified every time the data changes in the specified subtree.
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val post = p0.getValue(Post::class.java)
                if (post != null) {
                    //savePostToFirebaseDatabase에서 setValue한 형식대로 get을 한다.
//                    adapter.add(adapter.itemCount, UserItem(post))
                    Log.d("fetchPost","${adapter.itemCount}")
                    adapter.add(0, UserItem(post))

                }
                adapterNotifyDataSetChanged()
                //각 post들을 클릭했을 때 나오는 화면
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, PostLogActivity::class.java)
                    intent.putExtra(POST_KEY, userItem.post)
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

//Item은  com.xwray.groupie에 정의된 타입으로  그냥 받아들이면 됨
class UserItem(val post: Post) : Item<ViewHolder>() {
    //여기서 return한 layout 파일의 형식대로 recycler view에 추가됨.
    override fun getLayout(): Int {
        return R.layout.post_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.itemView까지 하면 view를 얻는다고 보면 됨.
        viewHolder.itemView.tvLectureName_post_row.text = "[${post.lecturename}]"
        viewHolder.itemView.tvProfessorName_post_row.text = "[${post.professorName}]"

        //TODO 사진 업로드. 프로필 이미지 업로드 이렇게 하면 됨.
        //Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.ivPostImage)
    }

}


@Parcelize
class Post(
    var postname: String,
    var lecturename: String,
    var professorName: String,
    var title: String,
    var year: Int,
    var test: Int,
    var service: Int,
    var reward: Double,
    var vote: Int,
    var author: String,
    var contents: String,
    var data : String
) : Parcelable {
    constructor() : this(
        "postName", "과목명", "교수명",
        "title", -1, -1, -1, -1.0, 0, "", "contents",""
    )
}
