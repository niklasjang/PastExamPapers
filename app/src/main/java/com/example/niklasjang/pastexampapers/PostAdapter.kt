package com.example.niklasjang.pastexampapers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.niklasjang.pastexampapers.R.id.tvPostProfessorName

class PostAdapter(private var activity : Activity, private var posts :ArrayList<Post> ) : BaseAdapter(){


    private class ViewHolder(row: View?) {
        var tvPostClassName: TextView? = null
        var tvPostProfessorName: TextView? = null
        var tvTagYear: TextView? = null
        var tvTagSemester: TextView? = null
        var tvTagTest : TextView? = null
        var tvDollar: TextView? = null
        var tvVote: TextView? = null
        var tvComment: TextView? = null
        var tvScrap: TextView? = null
        var tvAuthor : TextView? = null

        init {
            this.tvPostClassName = row?.findViewById(R.id.tvPostClassName)
            this.tvPostProfessorName = row?.findViewById(R.id.tvPostProfessorName)
            this.tvTagYear= row?.findViewById(R.id.tvTagYear)
            this.tvTagSemester = row?.findViewById(R.id.tvTagSemester)
            this.tvTagTest = row?.findViewById(R.id.tvTagTest)
            this.tvDollar = row?.findViewById(R.id.tvDollar)
            this.tvVote = row?.findViewById(R.id.tvVote)
            this.tvComment = row?.findViewById(R.id.tvComment)
            this.tvScrap = row?.findViewById(R.id.tvScrap)
            this.tvAuthor = row?.findViewById(R.id.tvAuthor)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.post_list_row, parent, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val selectedPost = posts[position] //userCA means userCheckingAccount
        viewHolder.tvPostClassName?.text = "[${selectedPost.className}]"
        viewHolder.tvPostProfessorName?.text = "${selectedPost.professorName} 교수님"
        when(selectedPost.year){

            1 -> viewHolder.tvTagYear?.text=  "1학년"
            2 -> viewHolder.tvTagYear?.text=  "2학년"
            3 -> viewHolder.tvTagYear?.text=  "3학년"
            4 -> viewHolder.tvTagYear?.text=  "4학년"
            else ->{
                viewHolder.tvTagYear?.text=  "Error"
            }
        }

        when(selectedPost.semester){
            1 -> viewHolder.tvTagSemester?.text= "1학기"
            2 -> viewHolder.tvTagSemester?.text= "2학기"
            3 -> viewHolder.tvTagSemester?.text= "1/2학기"
            else ->{
                viewHolder.tvTagSemester?.text= "Error"
            }
        }

        when(selectedPost.test){
            1 -> viewHolder.tvTagTest?.text=  "중간"
            2 -> viewHolder.tvTagTest?.text=  "기말"
            3 -> viewHolder.tvTagTest?.text=  "중간/기말"
            else ->{
                viewHolder.tvTagYear?.text=  "Error"
            }
        }
        viewHolder.tvDollar?.text ="$${selectedPost.reward}"
        viewHolder.tvVote?.text = "${selectedPost.vote}"
        viewHolder.tvComment?.text ="tvComment"
        viewHolder.tvScrap?.text = "${selectedPost.scrap}"
        viewHolder.tvAuthor?.text = "${selectedPost.author}"

        //vote 기능 구현
        viewHolder.tvVote?.setOnClickListener {view->
            when (selectedPost.vote){
                 0 -> selectedPost.vote = 1
                 1 -> selectedPost.vote = 0
            }
            //모든 개별 사용자의 vote는 0 1 왔다갔다. post의 vote는 계속 1씩 증가하는 걸로 구현 예정
            this.notifyDataSetChanged()
        }
        viewHolder.tvComment?.setOnClickListener { view->
            //댓글창 구현 예정 layout inflator 사용?
        }
        viewHolder.tvScrap?.setOnClickListener { view->
            //퍼가기 기능 구현 예정. DB연동 필요
            when (selectedPost.scrap) {
                true -> selectedPost.scrap = false
                false -> selectedPost.scrap = true
            }
            this.notifyDataSetChanged()
        }

        return view as View
    }

    override fun getItem(idx: Int): Post {
        return posts[idx]
    }

    override fun getItemId(idx: Int): Long {
        // ID는 항상 long 타입
        return idx.toLong()
    }

    override fun getCount(): Int {
        return posts.size
    }
}