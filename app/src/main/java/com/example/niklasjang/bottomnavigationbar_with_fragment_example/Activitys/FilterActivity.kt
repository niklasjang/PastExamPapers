package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.UserItem
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R.id.*
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_filter2.*

//values의 arrays에 spinner 값들이 존재한다. 또, manifest에서 해당 액티비티의 자판기가 레이아웃을 밀어버리는 일을 막는 코드를 넣었다.
class FilterActivity : AppCompatActivity() {
    var filterArray = arrayListOf<Post>()
    var valueArray = arrayListOf<Post>()
    val adapter = GroupAdapter<ViewHolder>()
    var recyclerView: RecyclerView? = null
    val ref = FirebaseDatabase.getInstance().getReference("/posts/")
    var firstOneSpinner : String? = null
    var secondSpinner : String? = null
    var filterDataFirst : String = "lecturename"
    var filterDateSecond : String = "year"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter2)
        recyclerView = findViewById<RecyclerView>(R.id.rv_filter)
        recyclerView?.adapter = adapter
        fetchPost()
//        rv_filter.adapter = ReadRecyclerViewAdaper(arrayList)
//        rv_filter.layoutManager = LinearLayoutManager(this)

        //spinner -> 강의명, 교수명
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                firstOneSpinner = p0!!.getItemAtPosition(p2) as String
                if(firstOneSpinner =="강의명"){
                    filterDataFirst = "lecturename"
                }else{
                    filterDataFirst = "professorName"
                }
            }
        }
        //spinner -> 학년, 중간고사(기말고사)
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                secondSpinner = p0!!.getItemAtPosition(p2) as String
                if(secondSpinner =="학년"){
                    filterDateSecond = "year"
                }else{
                    filterDateSecond = "test"
                }
            }
        }

        et_lecturename_filter.addTextChangedListener(object : TextWatcher {
            //글자가 변경되었을 때
            override fun afterTextChanged(p0: Editable?) {
                if(et_professorname_filter.text.toString() == "" && et_lecturename_filter.text.toString() == ""){
                    filterArray.clear()
                    valueArray.clear()
                    fetchPost()
                }
                else if(et_professorname_filter.text.toString() == "" && et_lecturename_filter.text.toString() != ""){
                    valueArray.clear()
                    valueArray.addAll(filterArray)
                    search(p0.toString(),filterDataFirst)
                    //searchListLecture(p0.toString())
                }
                else if(et_professorname_filter.text.toString() != "" && et_lecturename_filter.text.toString() == ""){
                    valueArray.clear()
                    valueArray.addAll(filterArray)
                    search(et_professorname_filter.text.toString(),filterDateSecond)
                    //searchListProfessor(et_professorname_filter.text.toString())
                }
                else{
                    search(p0.toString(),filterDataFirst)
                    //searchListLecture(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        et_professorname_filter.addTextChangedListener(object : TextWatcher {
            //글자가 변경되었을 때
            override fun afterTextChanged(p0: Editable?) {
                if(et_professorname_filter.text.toString() == "" && et_lecturename_filter.text.toString() == ""){
                    filterArray.clear()
                    valueArray.clear()
                    fetchPost()
                }
                else if(et_professorname_filter.text.toString() == "" && et_lecturename_filter.text.toString() != ""){
                    valueArray.clear()
                    valueArray.addAll(filterArray)
                    search(et_lecturename_filter.text.toString(),filterDataFirst)
                    //searchListLecture(et_lecturename_filter.text.toString())
                }
                else if(et_professorname_filter.text.toString() != "" && et_lecturename_filter.text.toString() == ""){
                    valueArray.clear()
                    valueArray.addAll(filterArray)
                    search(p0.toString(),filterDateSecond)
                    //searchListProfessor(p0.toString())
                }
                else{
                    //searchListProfessor(p0.toString())
                    search(p0.toString(),filterDateSecond)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }
    //실험
    fun search(filterString : String, filter : String){
        //filter는 ArrayList에 대한 반복문이다. 하나하나 다 함수를 실행한다.
        var filterList : List<Post>? = null
        if(filter == "lecturename"){
            filterList = valueArray.filter { post ->
                //userDTO.name!!.contains(filterString)
                checkCharacter(post.lecturename,filterString)
            }
        }
        else if(filter == "professorName"){
            filterList = valueArray.filter { post ->
                //userDTO.name!!.contains(filterString)
                checkCharacter(post.professorName,filterString)
            }
        }

        else if(filter == "grade"){
            filterList = valueArray.filter { post ->
                //userDTO.name!!.contains(filterString)
                checkCharacter(post.grade.toString(),filterString)
            }
        }

        else if(filter == "test"){
            filterList = valueArray.filter { post ->
                //userDTO.name!!.contains(filterString)
                checkCharacter(post.test.toString(),filterString)
            }
        }
        valueArray.clear()
        valueArray.addAll(filterList!!)
        //arrayList = filterList as ArrayList<UserDTO> 하지말자 이러면 포인터 값이 바뀌어서 adpater에서 제대로 인식하지 못한다.
        adapterNotifyDataSetChanged(valueArray)
    }
    //위의 코드가 안되면 아래를 실행하도록하자
//
//    fun searchListLecture(filterString : String){
//        //filter는 ArrayList에 대한 반복문이다. 하나하나 다 함수를 실행한다.
//        var filterList = valueArray.filter { post ->
//            //userDTO.name!!.contains(filterString)
//            checkCharacter(post.lecturename,filterString)
//        }
//        valueArray.clear()
//        valueArray.addAll(filterList)
//        //arrayList = filterList as ArrayList<UserDTO> 하지말자 이러면 포인터 값이 바뀌어서 adpater에서 제대로 인식하지 못한다.
//        adapterNotifyDataSetChanged(valueArray)
//    }
//
//    fun searchListProfessor(filterString : String){
//        //filter는 ArrayList에 대한 반복문이다. 하나하나 다 함수를 실행한다.
//        var filterList = valueArray.filter { post ->
//            //userDTO.name!!.contains(filterString)
//            checkCharacter(post.professorName,filterString)
//        }
//        valueArray.clear()
//        valueArray.addAll(filterList)
//        //arrayList = filterList as ArrayList<UserDTO> 하지말자 이러면 포인터 값이 바뀌어서 adpater에서 제대로 인식하지 못한다.
//        adapterNotifyDataSetChanged(valueArray)
//    }

    //검색 기능 중에 or기능 담당 ex) 강의명: 항공제어 데이터베이스 라고 검색 시에 동시에 검색된다.
    fun checkCharacter(name :String, searchString : String) : Boolean {
        //john sophia => arrayOf("john","sophia")
        //즉 검색하려는 것을 j k 라고 쓰면 j와 k에 관한 내용이 모두 나온다.
        var array = searchString.split(" ")
        for(item in array){
            if(name.contains(item)){
                return true
            }
        }
        return false
    }

    fun adapterNotifyDataSetChanged(array : ArrayList<Post>?){
        if(array != null){
            adapter.clear()
            for(item in array!!){
                adapter.add(UserItem(item))
            }
            recyclerView?.adapter?.notifyDataSetChanged()
        }
    }
    //recyclerview를 만들어준다.
    private fun fetchPost() {
        //If the addValueEventListener() method is used to add the listener,
        //the app will be notified every time the data changes in the specified subtree.
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val post = p0.getValue(Post::class.java)
                if (post != null) {
                    filterArray.add(post)
                    valueArray.add(post)
                    //savePostToFirebaseDatabase에서 setValue한 형식대로 get을 한다.
//                    adapter.add(adapter.itemCount, UserItem(post))
                    adapter.add(0, UserItem(post))
                }
                adapterNotifyDataSetChanged(valueArray)
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
