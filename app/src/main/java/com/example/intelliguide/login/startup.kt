package com.example.intelliguide.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.intelliguide.MainActivity
import com.example.intelliguide.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class startup : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var enterPassword: EditText
    private lateinit var btnregister: TextView
    private lateinit var loSignin: Button
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@startup, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        auth = Firebase.auth

        editTextEmail = findViewById<EditText>(R.id.loginEmail)
        enterPassword = findViewById<EditText>(R.id.loginPassword)
        btnregister = findViewById<TextView>(R.id.loginSignup)
        loSignin = findViewById<Button>(R.id.loginSignIn)


        btnregister.setOnClickListener {
            val intent = Intent(this@startup, touristReg::class.java)
            startActivity(intent)

        }

        loSignin.setOnClickListener {
            var email: String = ""
            var password: String = ""

            email = editTextEmail.text.toString()
            password = enterPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@startup, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
                //return

            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@startup, "Enter a Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            applicationContext,
                            "Authentication Pass.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        val intent = Intent(this@startup, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
}