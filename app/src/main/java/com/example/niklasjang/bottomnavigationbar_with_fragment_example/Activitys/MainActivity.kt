package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
import android.os.Build.ID
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.ActionMenuItemView
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.Toast
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.MyAccuontFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.NewsFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.Post
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.Key
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.ShowInfor
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.ShowInfor2
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

lateinit var Key_Save_ref: DatabaseReference
lateinit var Show_ref : DatabaseReference
lateinit var Vote_ref:DatabaseReference
lateinit var Vote_Transaction_ref: DatabaseReference
lateinit var Vote_User_ref: DatabaseReference
lateinit var Show_User_ref: DatabaseReference

lateinit var Key_List: MutableList<Key>
lateinit var Sub_List: MutableList<ShowInfor>
lateinit var Plus_List:MutableList<ShowInfor2>
lateinit var List :MutableList<Post>

lateinit var UserId: String
lateinit var plainID: String
lateinit var HashID: String

var Coin: Int =0
var Id:Int = 0 //클라이언트가 가지고 있는 고유 아이디
var First_Login: Int = 0 //처음 로그인 했는지 판단
var Third_Check : Int=0
var Fore_Check: Int=0


class MainActivity : AppCompatActivity() {

    //Navigation bar 이동 listener
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment?
        println("TEST 1234 $Coin")
        when (item.itemId) {
            R.id.navigation_news -> {

                fragment = NewsFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_timeline -> {
                fragment = TimelineFragment()
                loadTimelineFragment(fragment)
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

        verifyUserIsLoggedIn() //로그인 했는지 확인
        loadFragment(NewsFragment()) //어플 실행하자마자 보이는 화면 설정

        UserId = FirebaseAuth.getInstance().uid!! //firebase에 저장되어 있는 클라인트의 고유 정보 UID
        plainID = UserId.substring(0, 16)         //private_key는 16의 크기로 제한되어 있다 , 암호화 할때 private_key로 쓰임

        Key_Save_ref = FirebaseDatabase.getInstance().getReference("Key")
        Show_ref=FirebaseDatabase.getInstance().getReference("Sub")
        Vote_ref=FirebaseDatabase.getInstance().getReference("Plus")
        Vote_Transaction_ref=FirebaseDatabase.getInstance().getReference("Vote") //서버에 저장 되어 있는 보팅 트랜젝션을 참조
        Vote_User_ref=FirebaseDatabase.getInstance().getReference("Vote_User_id")
        Show_User_ref= FirebaseDatabase.getInstance().getReference("posts")

        Sub_List= mutableListOf()
        List= mutableListOf()
        Key_List = mutableListOf()
        Plus_List= mutableListOf()

        First_Login=0
        Third_Check =0
        Fore_Check=0

        getKey()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    //로그인 했는지 확인
    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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

                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    if(First_Login==1){
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
                            Coin=h.coin
                            HashID=h.hashID
                            First_Login = 1

                        }
                    }

                    if (First_Login != 1) {

                        val heroId = Key_Save_ref.push().key
                        val num = Key_List[Key_List.lastIndex].id.toInt()
                        val hero = Key((num + 1).toString(), UserId, 30,heroId!!)

                        Key_Save_ref.child(heroId!!).setValue(hero).addOnCompleteListener() {
                            Toast.makeText(applicationContext, "Hero saved sucessfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val heroId = Key_Save_ref.push().key
                    val hero = Key("1", UserId, 30,heroId!!)
                    Key_Save_ref.child(heroId!!).setValue(hero).addOnCompleteListener() {
                        Toast.makeText(applicationContext, "Hero saved sucessfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }


    //상단 menu bar 생성하기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
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
                startActivity(intent)
            }
            R.id.menu_refresh ->{
                val currentTimelineFragment  = supportFragmentManager.findFragmentByTag("CurrentTimelineFragment")
                if(currentTimelineFragment ==null) return false
                (currentTimelineFragment as TimelineFragment).fetchPost()

//                val rotate = RotateAnimation(
//                    0f, 360f,
//                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
//                )
//                rotate.duration = 1000
//                rotate.repeatCount = Animation.INFINITE
//                rotate.repeatMode = Animation.INFINITE
//                rotate.interpolator = LinearInterpolator()
//                val btnRefresh = findViewById<ActionMenuItemView>(R.id.menu_refresh)
//                btnRefresh.startAnimation(rotate)
//                btnRefresh.clearAnimation()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
