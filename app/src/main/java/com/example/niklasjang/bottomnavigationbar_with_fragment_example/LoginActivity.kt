package com.example.niklasjang.bottomnavigationbar_with_fragment_example

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        performLogin()
        tvBackToRegister_login.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(){
        btnLogin.setOnClickListener {
            var email = etEmail_login.text.toString()
            var password = etPassword_login.text.toString()

            if( email.isEmpty() || password.isEmpty()) return@setOnClickListener

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if( !it.isSuccessful) return@addOnCompleteListener
                    Log.d("Login Activity", "Login Success")
                    Toast.makeText(this, "Login Succeessed", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener{
                    Log.d("Login Activity", "Login Failed")
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    return@addOnFailureListener
                }

        }


    }
}
