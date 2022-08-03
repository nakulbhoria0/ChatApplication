package com.example.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var dbRef: DatabaseReference
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var senderRoom:String
    private lateinit var receiverRoom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val friendName = intent.getStringExtra("name")
        val receiverUID = intent.getStringExtra("uid")
        val senderUID = FirebaseAuth.getInstance().currentUser?.uid
        supportActionBar?.title = friendName


        senderRoom = "$receiverUID$senderUID"
        receiverRoom = "$senderUID$receiverUID"

        // initialize all variables
        initializeVariables()

        // getData from DB
        getDataFromDB()

        // logic for sending message
        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()
            messageBox.setText("")

            if(messageText.isNotEmpty()){
                val message = Message(messageText, senderUID)
                dbRef.child("chat").child(senderRoom).child("messages").push()
                    .setValue(message).addOnSuccessListener(this) {
                        dbRef.child("chat").child(receiverRoom).child("messages")
                            .push().setValue(message)
                    }
            }

        }

    }

    private fun initializeVariables(){

        dbRef = FirebaseDatabase.getInstance().reference

        messageList = ArrayList()
        messageAdapter = MessageAdapter(messageList)

        chatRecyclerView = findViewById<RecyclerView?>(R.id.chatRecyclerView).apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }
        sendButton = findViewById(R.id.ivSend)
        messageBox = findViewById(R.id.etMessage)
    }

    private fun getDataFromDB(){
        //logic for messageRecyclerView
        dbRef.child("chat").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(dataSnapshot in snapshot.children){
                        val message = dataSnapshot.getValue(Message::class.java)
                        if (message != null) {
                            messageList.add(message)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}