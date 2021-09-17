package com.example.chatting.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatting.R
import com.example.chatting.adapter.UserAdapter
import com.example.chatting.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    var userlist = ArrayList<User>()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        recyclerviewuser.layoutManager = LinearLayoutManager(this , LinearLayout.VERTICAL , false)


        imgback.setOnClickListener {
            onBackPressed()
        }


        imgprofile.setOnClickListener {
            val intent = Intent (this@UserActivity ,
            ProfileActivity::class.java)
            startActivity(intent)
        }
        getUserList()
    }

    fun getUserList(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        var databaseReference:DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                val currentUser = snapshot.getValue(User::class.java)

                if(currentUser!!.profileImage == "") {
                    imgprofile.setImageResource(R.drawable.hantu)
                } else {
                    Glide.with(this@UserActivity).load(currentUser.profileImage).into(imgprofile)
                }

                for (dataSnapshot:DataSnapshot in snapshot.children){
                    val user = dataSnapshot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {
                        userlist.add(user)
                    }
                }
                val userAdapter = UserAdapter (this@UserActivity , userlist)
                recyclerviewuser.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()

            }

        })
    }
}