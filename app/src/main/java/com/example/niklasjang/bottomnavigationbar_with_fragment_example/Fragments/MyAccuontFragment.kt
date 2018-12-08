package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.PreferenceActivity
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R


class MyAccuontFragment : Fragment() {
    var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_accuont, container, false)
        val btnSettings = view.findViewById<Button>(R.id.btnSettings_my_account)

        btnSettings.setOnClickListener {
            val intent = Intent(view.context, PreferenceActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}
