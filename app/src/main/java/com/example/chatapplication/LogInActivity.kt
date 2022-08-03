package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var btLogin:Button
    private lateinit var btSignUp:Button

    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btLogin = findViewById(R.id.btLogin)
        btSignUp = findViewById(R.id.btSignUp)


        btSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }

        btLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            login(email, password)

        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                Toast.makeText(this, "${user?.email} Successfully Logged In",
                    Toast.LENGTH_SHORT).show()
                startMainActivity()

            }else{
                Toast.makeText(this, "Log In Failed",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            startMainActivity()
        }
    }
    private fun startMainActivity(){
        val intent = Intent(this@LogInActivity, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}