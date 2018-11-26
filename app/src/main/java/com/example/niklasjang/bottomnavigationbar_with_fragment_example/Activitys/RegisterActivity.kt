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
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
//import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.parcel.Parcelize


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
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    //addOnCompleteLister is performed when the task is done regardless of success or not.
                    Toast.makeText(this, "createUserWithEmail Successful", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterActivity", "Successfully created user with uid : ${it.result?.user?.uid}")

                    upLoadImageToFirebaseStorage()
                    var intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "createUserWithEmail Failed ${exception.message}", Toast.LENGTH_LONG).show()
                    Log.d("RegisterActivity", "Failed to create user because of ${exception.message}")
                    return@addOnFailureListener // return@methodName : explicitly specify whew you return.
                }

        }

        //Google Login
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this,gso)

        btGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            startActivityForResult(signIntent,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Firebase 로그인
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

        //구글 로그인
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            //구글 로그인에 성공했을 때 넘어오는 토큰 값을 가지는 task
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            //ApiException 캐스팅
            var account = task.getResult(ApiException::class.java)
            //credential -> 구글 로그인에 성공했다는 인증서
            var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
            //firebase에 계정 등록
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"구글 계정 연동에 성공하였습니다.",Toast.LENGTH_LONG).show()

                        // TODO 사용자의 계정 정보는 아래의 코드로 가져온다. 구글 로그인 했을 때 구글 아이디 가져오는지 확인해야함.
                        // TODO FirebaseAuth.getInstance().currentUser

                        var intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this,"구글 계정 연동에 실패하였습니다.",Toast.LENGTH_LONG).show()
                    }
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
}
@Parcelize
class User(val uid : String, val username : String, val profileImageUrl : String) : Parcelable{
    constructor() : this("","","")
}