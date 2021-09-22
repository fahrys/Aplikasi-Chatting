package com.example.chatting.activity

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chatting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.imgprofile
import kotlinx.android.synthetic.main.activity_user.*

class ChatActivity : AppCompatActivity() {
    var firebaseUser:FirebaseUser? = null
    var reference:DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var intent = getIntent()
        var userId = intent.getStringExtra("userId")

        val img = findViewById<ImageView>(R.id.imgback)
        img.setOnClickListener {
            onBackPressed()
        }


        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(com.example.chatting.model.User::class.java)
                tv_UserName.text = user!!.userName
                if(user.profileImage == "") {
                    imgprofile.setImageResource(R.drawable.hantu)
                }else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgprofile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btn_message.setOnClickListener{
            var message:String = et_message.text.toString()

            if (message.isEmpty()){
                Toast.makeText(applicationContext , "message is empty", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(firebaseUser!!.uid , userId, message)
            }
        }
    }

    private fun sendMessage(senderId:String , receiverId:String , message:String){
        val reference:DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId" , senderId)
        hashMap.put("receiverId" , receiverId)
        hashMap.put("message" , message)

        reference!!.child("chat").push().setValue(hashMap)

    }
}