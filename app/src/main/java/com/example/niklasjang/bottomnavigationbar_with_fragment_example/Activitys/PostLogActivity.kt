package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.post_entry.view.*

/*
1. class post에 title 추가
2. MakePostActivity adapter 선언 fetchpost 밖으로 꺼내기
 */

class PostLogActivity : AppCompatActivity() {
    //    val adapter = GroupAdapter<ViewHolder>()
//    var recyclerView : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_log)
        //TODO SET MAX WIDth in xml
//        android:MaxWidth
        val post = intent.getParcelableExtra<Post>(TimelineFragment.POST_KEY)
        supportActionBar?.title = post.lecturename

        val adapter = GroupAdapter<ViewHolder>()
        val recyclerView : RecyclerView? = findViewById(R.id.recyclerview_post_log)
        recyclerView?.adapter = adapter
        adapter.add(PostEntryItem(post))
        adapter.notifyDataSetChanged()
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


//Item은  com.xwray.groupie에 정의된 타입으로  그냥 받아들이면 됨
class PostEntryItem(val post: Post) : Item<ViewHolder>() {
    //여기서 return한 layout 파일의 형식대로 recycler view에 추가됨.
    override fun getLayout(): Int {
        return R.layout.post_entry
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.itemView까지 하면 view를 얻는다고 보면 됨.
        viewHolder.itemView.tvLecturename_post_entry.text = "[${post.lecturename}]"
        viewHolder.itemView.tvProfessorname_post_entry.text = "${post.professorName}교수님"
        viewHolder.itemView.tvService_post_entry.text = "${post.service}"
        viewHolder.itemView.tvYear_post_entry.text = "${post.year}학년"
        viewHolder.itemView.tvTest_post_entry.text = "[${post.test}시험"
        viewHolder.itemView.tvTitle_post_entry.text = "[${post.title}]"
        viewHolder.itemView.tvComment_post_entry.text = "[${post.contents}]"
        viewHolder.itemView.tvUsername_post_entry.text = "[${post.author}]"
        viewHolder.itemView.tvReward_post_entry.text = "[${post.reward}]"
        viewHolder.itemView.tvVote_post_entry.text = "[${post.vote}]"
//        viewHolder.itemView.tvDate_post_entry.text = "[${post.data}]"
        //get CommentItemCount
//        viewHolder.itemView.tvComment_post_entry.text = "[${post.c}]"


        //TODO 사진 업로드. 프로필 이미지 업로드 이렇게 하면 됨.

//        post.uid
        //Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.ivPostImage)
    }

}