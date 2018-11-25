package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment :Fragment? = null
        when (item.itemId) {
            R.id.navigation_news -> {
                fragment = NewsFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_timeline-> {
                fragment = TimelineFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_my_account-> {
                fragment = MyAccuontFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings-> {
                fragment = SettingsFragment()
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

    private fun verifyUserIsLoggedIn(){
        var uid = FirebaseAuth.getInstance().uid
        if ( uid == null){
            var intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun loadFragment(fragment : Fragment) : Boolean{
        if(fragment != null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }


    //상단 menu bar 생성하기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when( item?.itemId ){
            R.id.menu_new_post-> {
                /*TODO 어떤 fragment에서 넘어온 건지 기억해서 돌아가기. 지금은 manifests에 parent actiyivty만 설정했음.
                  TODO 그래서 Main Activit가 처음 시작될 때 News Fragment가 시작되게 설정한 것이 자동으로 시작됨.
                */
                val intent = Intent(this, MakePostActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out ->{
                //TODO 새로운 구글 아이디로 로그인하게 만들어야 함
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
