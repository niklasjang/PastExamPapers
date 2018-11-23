package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
//import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        performRegister()
        tvAlreadyHaveAccount_register.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        btnSelectImage.setOnClickListener {
            Log.d("RegisterActivity", "Select Image clicked")
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun performRegister() {
        btnRegister.setOnClickListener {
            val email = etEmail_register.text.toString()
            val password = etPassword_register.text.toString()
            //아래 부분이 없으면 입력없이 btnRegister 클릭했을 때 어플이 깨진다.
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter text in email/password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Firebase Atuthentication to create a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    //addOnCompleteLister is performed when the task is done regardless of success or not.
                    Toast.makeText(this, "createUserWithEmail Successful", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterActivity", "Successfully created user with uid : ${it.result?.user?.uid}")

                    upLoadImageToFirebaseStorage()
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "createUserWithEmail Failed ${exception.message}", Toast.LENGTH_LONG).show()
                    Log.d("RegisterActivity", "Failed to create user because of ${exception.message}")
                    return@addOnFailureListener // return@methodName : explicitly specify whew you return.
                }

        }

    }

    private fun upLoadImageToFirebaseStorage() {


        if (selectedPhotoUri == null) return
        var filename = UUID.randomUUID().toString()
        //"/images/ specify where image will be stored
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
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
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl : String) {
        //every time this method is executed, correct uid is assigned to here.
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.setValue(User(uid, etUsername_register.text.toString(), profileImageUrl, etPassword_register.text.toString()))
            .addOnSuccessListener {
                Log.d("Register Activity", "Finally we saved the User to Firebase Database ")
            }
            .addOnFailureListener {
                Log.d("Register Activity", "Badly we can't saved the User to Firebase Database : $it ")

            }
    }
}

class User(val uid : String, val username : String, val profileImageUrl : String, val password : String)