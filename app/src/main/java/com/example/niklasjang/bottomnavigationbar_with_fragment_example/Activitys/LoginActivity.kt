package com.example.niklasjang.bottomnavigationbar_with_fragment_example.Activitys

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.niklasjang.bottomnavigationbar_with_fragment_example.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    var gso: GoogleSignInOptions? = null
    var googleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        performLogin()
        googleLoginSetup()
        tvGoToCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            Log.d("LogTest","call Register Activity, requestCode is 300")
            startActivityForResult(intent,300)
        }
    }

    //Firebase 로그인 실행
    private fun performLogin() {
        btnLogin.setOnClickListener {
            val email = etEmail_login.text.toString()
            val password = etPassword_login.text.toString()

            //입력칸 채워져있지 않으면 어플 깨져서 꼭 필요한 부분
            if (email.isEmpty() || password.isEmpty()) return@setOnClickListener

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Login Activity", "Login Success")
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
                    setResult(201)
                    finish()
                    Log.d("LogTest","call Main Activity. resultcode is 201")
                }
                .addOnFailureListener {
                    Log.d("Login Activity", "Login Failed")
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                    return@addOnFailureListener
                }
        }
    }

    private fun googleLoginSetup() {
        //Google Login

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso!!)
        btGoogle.setOnClickListener {
            val signIntent = googleSignInClient?.signInIntent
            startActivityForResult(signIntent, 1)
        }

    }

    //구글 로그인 버튼 누르기 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==300 && resultCode==301){
            Log.d("LogTest","Back to Main Activity, resultCode is 201")
            setResult(201)
            finish()
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //구글 로그인에 성공했을 때 넘어오는 토큰 값을 가지는 task
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            //ApiException 캐스팅
            val account = task.getResult(ApiException::class.java)
            //credential -> 구글 로그인에 성공했다는 인증서
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            //firebase에 계정 등록
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "구글 계정 연동에 성공하였습니다.", Toast.LENGTH_LONG).show()

                        //구글 버튼을 클릭할 때마다 다른 아이디로 로그인할 수 있도록 설정해주는 부분
                        googleSignInClient?.revokeAccess()

                        // TODO 사용자의 계정 정보는 아래의 코드로 가져온다. 구글 로그인 했을 때 구글 아이디 가져오는지 확인해야함.
                        // TODO FirebaseAuth.getInstance().currentUser?.UId로 가져올 수 있음을 확인했음.
                        // Log.d("Login Activity", "what? ${FirebaseAuth.getInstance().currentUser?.uid}")

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)


                    } else {
                        Toast.makeText(this, "구글 계정 연동에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}
