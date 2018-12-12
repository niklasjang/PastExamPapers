package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.os.Build.ID
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.MyAccuontFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.NewsFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.*
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_my_accuont.*

lateinit var Post_Transaction_ref: DatabaseReference
lateinit var Key_Save_ref: DatabaseReference
lateinit var Show_ref : DatabaseReference
lateinit var Vote_ref:DatabaseReference
lateinit var Vote_Transaction_ref: DatabaseReference

lateinit var Post_Process_ref : DatabaseReference
lateinit var Vote_User_ref: DatabaseReference
lateinit var Show_User_ref: DatabaseReference
lateinit var Coin_Transaction_List: MutableList<Hero>

lateinit var Key_List: MutableList<Key>
lateinit var Sub_List: MutableList<ShowInfor>
lateinit var Plus_List:MutableList<ShowInfor2>
lateinit var List :MutableList<Post>
lateinit var Post_List:MutableList<Vote>

lateinit var UserId: String
lateinit var plainID: String
lateinit var HashID: String
lateinit var pass :String

var Coin: Double =0.0
var Id:Int = 0 //클라이언트가 가지고 있는 고유 아이디
var First_Login: Int = 0 //처음 로그인 했는지 판단
var Second_Check : Int=0
var Third_Check : Int=0
var Fore_Check: Int=0
var Five_Check: Int=0
var Six_Check : Int=0


class MainActivity : AppCompatActivity() {

    //Navigation bar 이동 listener
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment?

        when (item.itemId) {
            R.id.navigation_news -> {

                fragment = NewsFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_timeline -> {


                fragment = TimelineFragment()
                loadTimelineFragment(fragment)
//                Toast.makeText(this,"click $Coin",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_my_account -> {
                fragment = MyAccuontFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Question Coin"

        Post_Transaction_ref = FirebaseDatabase.getInstance().getReference("Post_tx") //서버에 저장되어 있는 코인의 이동(코인 획득, 소모)의 트랜젝션을 참조
        Key_Save_ref = FirebaseDatabase.getInstance().getReference("Key") //서버에 저장 되어 있는 코인를 참조
        Show_ref=FirebaseDatabase.getInstance().getReference("Sub") //코인 소모 트랜젝션 참조
        Vote_ref=FirebaseDatabase.getInstance().getReference("Plus") // 코인 획득 트랜젝션 참조
        Vote_Transaction_ref=FirebaseDatabase.getInstance().getReference("Vote") //서버에 저장 되어 있는 보팅 트랜젝션을 참조
        Vote_User_ref= FirebaseDatabase.getInstance().getReference("posts") //게시물 참조
        Show_User_ref= FirebaseDatabase.getInstance().getReference("posts") //게시물 참조

        Coin_Transaction_List= mutableListOf()
        Sub_List= mutableListOf()
        List= mutableListOf()
        Key_List = mutableListOf()
        Plus_List= mutableListOf()
        Post_List= mutableListOf()

        First_Login=0
        Third_Check =0
        Fore_Check=0
        Five_Check=0


        verifyUserIsLoggedIn() //로그인 했는지 확인
        loadFragment(NewsFragment()) //어플 실행하자마자 보이는 화면 설정


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }



    //로그인 했는지 확인
    private fun verifyUserIsLoggedIn() {

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivityForResult(intent,200)
            Log.d("LogTest","call Login Activity, requestCode is 200")
        }else{
            Log.d("LogTest","you have already logged in...")

            UserId = uid
            getKey() //유저의 고유 정보 가져오기


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==201) {// 201은 레지스터에서 202는 로그인에서 넘어올 떄
            Log.d("LogTest","resultCode 201 받음")
            verifyUserIsLoggedIn()
        }
    }

    //Navigation bar 전환
    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null) //Remember past fragment when press back button
            .commit()
        return true
    }
    //Navigation bar 전환, TimelineFragment는 animatino을 구성하기위해 따로 뗌
    private fun loadTimelineFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment,"CurrentTimelineFragment")
//            .addToBackStack(null) //Remember past fragment when press back button
            .commit()

        return true
    }

    private fun getKey() { //key 생성, 처음 login 했을 때
        var name: String


        Key_Save_ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    if(First_Login ==1){
                        return
                    }

                    Key_List.clear()

                    for (h in p0.children) {
                        val hero = h.getValue(Key::class.java)
                        Key_List.add(hero!!)
                    }

                    for (h in Key_List) {
                        if (h.uid.equals(UserId)) {

                            Id = h.id.toInt()
                            Coin =h.coin
                            HashID =h.hashID
                            First_Login = 1

                        }
                    }

                    if (First_Login != 1) {

                        val heroId = Key_Save_ref.push().key
                        val num = Key_List[Key_List.lastIndex].id.toInt()
                        val hero = Key((num + 1).toString(), UserId, 0.0,heroId!!)

                        Key_Save_ref.child(heroId!!).setValue(hero).addOnCompleteListener() {
                        }
                    }
                } else {
                    val heroId = Key_Save_ref.push().key
                    val hero = Key("1", UserId, 0.0,heroId!!)
                    Key_Save_ref.child(heroId!!).setValue(hero).addOnCompleteListener() {
                    }
                }
            }
        })

    }



    //상단 menu bar 생성하기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //상단 menu bar select listener.
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_post -> {
                //TODO 어떤 fragment에서 넘어온 건지 기억해서 돌아가기. 지금은 manifests에 parent actiyivty만 설정했음.
                // TODO 그래서 Main Activit가 처음 시작될 때 News Fragment가 시작되게 설정한 것이 자동으로 시작됨.
                val intent = Intent(this, MakePostActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(intent)
                Log.d("LogTest", "Sign out.. ")
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //back btn을 2초 이내에 두 번 눌러야지 어플 종료
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}


