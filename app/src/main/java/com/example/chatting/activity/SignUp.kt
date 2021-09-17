package com.example.chatting.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.chatting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        btn_signup.setOnClickListener {
            val userName = etname.text.toString()
            val email = etemail.text.toString()
            val password = etpassword.text.toString()
            val confirmPassword = etconfirmpassword.text.toString()

            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(applicationContext , "username is required" , Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext , "email is required" , Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext , "password is required" , Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(applicationContext , "isi is required" , Toast.LENGTH_SHORT).show()
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(applicationContext , "Isi yg benar" , Toast.LENGTH_SHORT).show()
            }
            registerUser(userName,email, password)
        }

        btn_login.setOnClickListener {
            val intent = Intent (this@SignUp , Login::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun registerUser(userName:String , email:String , password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String , String> = HashMap()
                    hashMap.put("userId" , userId)
                    hashMap.put("userName" , userName)
                    hashMap.put("profileImage" , "")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){

                            etname.setText("")
                            etemail.setText("")
                            etpassword.setText("")
                            etconfirmpassword.setText("")
                            var intent = Intent(this@SignUp , UserActivity::class.java)
                        startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}