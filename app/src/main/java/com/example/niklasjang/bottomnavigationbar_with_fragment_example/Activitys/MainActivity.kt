package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.content.Intent
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
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.MyAccuontFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.NewsFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments.TimelineFragment
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

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
