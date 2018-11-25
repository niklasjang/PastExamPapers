package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_make_post.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import kotlinx.android.synthetic.main.post_list_row.view.*

class MakePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Make Post Activity에 reycler view 연습코드 적어둠.
        setContentView(R.layout.activity_make_post)
        supportActionBar?.title = "Make Post"
        fetchPost()
    }

    private fun fetchPost(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    //print All data at /users in firebase
                    Log.d("MakePost", it.toString())

                    //saveUserToFirebaseDatabase에서 setValue한 형식대로 get을 한다.
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }
                recyclerview_makePost.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
                //Toast.make ~~
            }
        })
    }
}

//param은  com.xwray.groupie에 정의된 타입으로  그냥 받아들이면 됨
class UserItem(val user : User) : Item<ViewHolder>(){
    //여기서 return한 layout 파일의 형식대로 recycler view에 추가됨.
    override fun getLayout(): Int {
        return R.layout.post_list_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.itemView까지 하면 view를 얻는다고 보면 됨.
        viewHolder.itemView.tvPostClassName.text = user.username
        //TODO 사진 업로드. 프로필 이미지 업로드 이렇게 하면 됨.
        //Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.ivPostImage)
    }

}
