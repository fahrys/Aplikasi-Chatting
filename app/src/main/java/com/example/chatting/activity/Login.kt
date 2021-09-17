package com.example.chatting.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.chatting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!

        if(firebaseUser != null) {
            val intent = Intent (this@Login ,
                UserActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login2.setOnClickListener {
            val email = etloginemail.text.toString()
            val password = etloginpassword.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext , "email dan password dibutuhkan", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            etloginemail.setText("")
                            etloginpassword.setText("")
                            val intent = Intent (this@Login ,
                                UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext , "email or password invalid" , Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        btn_signup2.setOnClickListener {
            val intent = Intent (this@Login , SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
}