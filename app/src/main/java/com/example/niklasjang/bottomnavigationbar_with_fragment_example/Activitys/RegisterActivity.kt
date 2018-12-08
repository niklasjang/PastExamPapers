package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.Models.User
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_login.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        tvAlreadyHaveAccount_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        btnSelectImage.setOnClickListener {
            Log.d("RegisterActivity", "Select Image clicked")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }
        performRegister()
    }

    //Register버튼과 Google 로그인 버튼 clickListener 정의하기.
    private fun performRegister() {
        //firebase login
        btnRegister.setOnClickListener {
            val email = etEmail_register.text.toString()
            val password = etPassword_register.text.toString()

            //아래 부분이 없으면 입력없이 btnRegister 클릭했을 때 어플이 깨진다.
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter text in email/password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Firebase Atuthentication to create a user with email and password
            //계정을 create 하면 uid가 할당이 되고 이 uid는 앞으로  FirebaseAuth.getInstance().uid ?: ""로 얻을 수 있다.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    //addOnCompleteLister is performed when the task is done regardless of success or not.
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Toast.makeText(this, "createUserWithEmail Successful", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterActivity", "Successfully created user with uid : ${it.result?.user?.uid}")

                    //main activity로 이동
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
                    Log.d("LogTest","Back to Login Activity, resultCode is 301")
                    setResult(301)
                    finish()
//  val intent = Intent(this, LoginActivity::class.java)
//                    val uid = FirebaseAuth.getInstance().currentUser?.uid
//                    intent.putExtra("uid",uid)
//                    startActivityForResult(intent,120)
//                    아이디 만들기 성공 후 선택한 이미지 파베에 올리기. + 유저정보를 DB에 저장하기
                    upLoadImageToFirebaseStorage()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "createUserWithEmail Failed ${exception.message}", Toast.LENGTH_LONG).show()
                    Log.d("RegisterActivity", "Failed to create user because of ${exception.message}")
                    return@addOnFailureListener // return@methodName : explicitly specify whew you return.
                }
        }


    }

    var profileImage_uri: String = ""

    private fun upLoadImageToFirebaseStorage() {

        val filename = UUID.randomUUID().toString() //랜덤값 정의하기
        //"/images/ specify where image will be stored

        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        if (selectedPhotoUri != null) {
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Successfully uploaded image : ${it.metadata?.path}")

                    //it is downloadUri where User can download uploaded image.
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Register Activity", "downloadUri : $it")
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed uploaded image : ${it.message}")
                }
        }else{
            saveUserToFirebaseDatabase("null")
        }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        //every time this method is executed, correct uid is assigned to here.
        profileImage_uri = profileImageUrl
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.setValue(
            User(
                uid,
                etUsername_register.text.toString(),
                profileImageUrl
            )
        )
            .addOnSuccessListener {
                Log.d("Register Activity", "Finally we saved the User to Firebase Database ")
            }
            .addOnFailureListener {
                Log.d("Register Activity", "Badly we can't saved the User to Firebase Database : $it ")

            }
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //사진 선택 intent 결과
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what the image selected was
            //uri represent the location data(image selected) is stored in device.
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            btnSelectImage.alpha = 0f // 0 is transparent, 225 is fully opaque. f means float. It taks float value.
            ivSelectedPhoto_register.setImageBitmap(bitmap)
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            btnSelectImage.setBackgroundDrawable(bitmapDrawable)
        }
    }

}

