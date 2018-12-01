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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.post_row.view.*


class TimelineFragment : Fragment() {
    companion object {
        val USER_KEY = "USER_KEY"
        val POST_KEY = "POST_KEY"
    }

    var btnFilter: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFilter = view.findViewById<Button>(R.id.btnFilter)
        btnFilter?.setOnClickListener {
            val intent = Intent(activity, FilterActivity::class.java)
            startActivityForResult(intent, 100)
        }
        fetchPost()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            //TODO 필터 구현
        }
    }

    private fun fetchPost() {
        val ref = FirebaseDatabase.getInstance().getReference("/posts/")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview_timeline)
                recyclerView?.adapter = adapter
                //TODO UI : divider 추가
                recyclerView?.addItemDecoration(DividerItemDecoration(view?.context, DividerItemDecoration.VERTICAL))
                p0.children.forEach {
                    //print All data at /posts in firebase
                    Log.d("MakePost", "here!@#${it}")

                    //savePostToFirebaseDatabase에서 setValue한 형식대로 get을 한다.
                    val post = it.getValue(Post::class.java)
                    if (post != null) {
                        adapter.add(UserItem(post))
                        
                    }
                }
                //각 post들을 클릭했을 때 나오는 화면
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, PostLogActivity::class.java)
                    intent.putExtra(POST_KEY, userItem.post)
                    startActivity(intent)
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                //Toast.make ~~
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
    var year: Int,
    var test: Int,
    var service: Int,
    var reward: Double,
    var vote: Int,
    var uid: String,
    var contents: String
) : Parcelable {
    constructor() : this("postName","과목명",
        "교수님", -1, -1, -1, -1.0, 0, "", "")
}
