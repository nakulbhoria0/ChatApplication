package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btSignUp: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()


        mAuth = FirebaseAuth.getInstance()

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btSignUp = findViewById(R.id.btSignUp)

        btSignUp.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val name = etName.text.toString()

            signUp(name, email, password)
        }

    }

    private fun signUp(name:String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                Toast.makeText(this, "${user?.email} Successfully Signed Up",
                    Toast.LENGTH_SHORT).show()
                addUserToDatabase(name, email, user?.uid)
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                finish()
                startActivity(intent)
            }else{
                Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String?) {
        databaseRef = FirebaseDatabase.getInstance().reference

        databaseRef.child("users").child(uid!!).setValue(User(name, email, uid))


    }
}