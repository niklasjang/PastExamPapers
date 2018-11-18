package com.example.niklasjang.questioncoin_structure

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils.replace
import android.util.Log
import com.example.niklasjang.questioncoin_structure.R.id.tvMyAccountFragment
import com.example.niklasjang.questioncoin_structure.R.id.tvSettingsFragment
import com.example.niklasjang.questioncoin_structure.R.layout.fragment_main
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_timeline.*

class MainActivity : AppCompatActivity() {
    /*
     1. 하나의 스텍만 남아서 back 버튼을 눌렀을 때 이전의 fragment로 가게하기
     2. Listview 말고 ListFragment로 구현했을 때 어떤 점이 달라지는가?
     */

    /*
      1.Replace whatever is in the fragment_container view with this fragment,
        and add the transaction to the back stack
      2.addToBackStack: commit()을 호출하기 전에 적용된 모든 변경 내용이 백 스택에 하나의 트랜잭션으로 추가되며,
        Back 버튼을 누르면 모두 한꺼번에 되돌려집니다. addToBackStack()을 호출하지 않는 경우,
        해당 프래그먼트는 트랜잭션이 적용되면 소멸되고 사용자가 이를 되짚어 탐색할 수 없게 됩니다.
        반면에 프래그먼트를 제거하면서 addToBackStack()을 호출하면, 해당 프래그먼트는 중단되고 사용자가 뒤로 탐색하면 재개됩니다.
    */
    /*
    private fun replaceFragment (fragment : Fragment){

        if( fragment != null) {
            var fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            //addOnBackStackChangedListener() : FragmentManager 클래스 관련 문서를 참조하세요.
        }else{
            Log.d("MainActivity", "Fragment is null")
        }
    }*/


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_main -> {
                Log.d("MainActivity", "item is navigation_main")
                var mainFragment = supportFragmentManager.findFragmentById(R.id.fragmentMain)
                if(mainFragment != null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, mainFragment)
                        .addToBackStack(null)
                        .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_timeline -> {
                Log.d("MainActivity", "item is navigation_timeline")
                tvTimelineFragment.text="@string/TimelineFragment"
                var timelineFragment = supportFragmentManager.findFragmentById(R.id.fragmentTimeline)
                if(timelineFragment != null) {
                    supportFragmentManager.beginTransaction()
                        .remove(supportFragmentManager.findFragmentById(R.id.fragmentMain)!!)
//                        .replace(R.id.container, timelineFragment)
                        .addToBackStack(null)
                        .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_my_account-> {
                Log.d("MainActivity", "item is navigation_myAccount")
                tvMyAccountFragment.text="@string/MyAccountFragment"
                var myAccountFragment = supportFragmentManager.findFragmentById(R.id.fragmentMyAccount)
                if(myAccountFragment != null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, myAccountFragment)
                        .addToBackStack(null)
                        .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings-> {
                Log.d("MainActivity", "item is navigation_settings")
                if( supportFragmentManager.findFragmentById(R.id.fragmentSettings) != null) {
//                    replaceFragment(supportFragmentManager.findFragmentById(R.id.fragmentSettings)!!)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }


}
