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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var editTextEmail: EditText
private lateinit var enterPassword: EditText
private lateinit var btnregister: TextView
private lateinit var loSignin: Button
private lateinit var auth: FirebaseAuth
private lateinit var dbRef: DatabaseReference

class login : AppCompatActivity() {

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@login, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        editTextEmail = findViewById<EditText>(R.id.loginEmail)
        enterPassword = findViewById<EditText>(R.id.TourRegAge)
        btnregister = findViewById<Button>(R.id.btnSignIn)
        loSignin = findViewById<Button>(R.id.btnSignUp)


        btnregister.setOnClickListener {
            val intent = Intent(this@login, userSelect::class.java)
            startActivity(intent)
        }

        loSignin.setOnClickListener {
            var email: String = ""
            var password: String = ""

            email = editTextEmail.text.toString()
            password = enterPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@login, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
                //return

            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@login, "Enter a Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(
                            applicationContext,
                            "Authentication Pass.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val userId = currentUser?.uid ?: ""

                        val query = FirebaseDatabase.getInstance().getReference("userModel")
                            .orderByChild("userId")
                            .equalTo(userId)



                        val intent = Intent(this@login, MainActivity::class.java)
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