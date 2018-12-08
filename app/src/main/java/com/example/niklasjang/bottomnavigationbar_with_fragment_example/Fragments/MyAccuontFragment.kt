package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Fragments


import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.PreferenceActivity
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.User
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_register.*
import android.content.ContentResolver
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Log
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.net.URL


//import sun.security.krb5.internal.KDCOptions.with





class MyAccuontFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        Log.d("profileImageUrl", " SSl BBAl  : ${RegisterActivity().profileImage_uri}")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_accuont, container, false)
        val btnSettings = view.findViewById<Button>(R.id.btnSettings_my_account)

        val profileImage = view.findViewById<CircleImageView>(R.id.profile_image)

        var url = ""
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val urlRef = FirebaseDatabase.getInstance().getReference("/users/$uid/profileImageUrl")
        urlRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("p0", "p0 : $p0")
//                url =p0.value.toString()
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })


//        Picasso.get().load(url).into(profileImage)





        btnSettings.setOnClickListener {
            val intent = Intent(view.context, PreferenceActivity::class.java)
            startActivity(intent)
        }
        return view
    }



}
