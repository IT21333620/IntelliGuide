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
import com.example.intelliguide.tourist.tourist_home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

private lateinit var enterEmail: EditText
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
            val userId = currentUser.uid

            val query = FirebaseDatabase.getInstance().getReference("userModel")
                .orderByChild("userId")
                .equalTo(userId)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Assuming there's only one matching record
                        for (childSnapshot in snapshot.children) {
                            val userType = childSnapshot.child("type").getValue(String::class.java)
                            // Check the user's type and navigate accordingly
                            when (userType) {
                                "Tourist" -> {
                                    val intent = Intent(this@login, tourist_home::class.java)
                                    startActivity(intent)
                                    finish() // Finish the current activity to prevent going back to login
                                    Toast.makeText(baseContext, "Logged in as Tourist.", Toast.LENGTH_SHORT).show()
                                }
                                "Hotel Owner" -> {
                                    val intent = Intent(this@login, startup::class.java)
                                    startActivity(intent)
                                    finish()
                                    Toast.makeText(baseContext, "Logged in as Hotel Owner.", Toast.LENGTH_SHORT).show()
                                }
                                "Tourist Police" -> {
                                    val intent = Intent(this@login, startup::class.java)
                                    startActivity(intent)
                                    finish()
                                    Toast.makeText(baseContext, "Logged in as Tourist Police.", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(baseContext, "Place Not Found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle an error here if needed
                }
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        enterEmail = findViewById<EditText>(R.id.loginEmail)
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

            email = enterEmail.text.toString()
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
                        // Authentication successful
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val userId = currentUser?.uid ?: ""

                        // Query to get the user's type
                        val query = FirebaseDatabase.getInstance().getReference("userModel")
                            .orderByChild("userId")
                            .equalTo(userId)

                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // Assuming there's only one matching record
                                    for (childSnapshot in snapshot.children) {
                                        val userType = childSnapshot.child("type").getValue(String::class.java)
                                        // Check the user's type and navigate accordingly
                                        when (userType) {
                                            "Tourist" -> {
                                                val intent = Intent(this@login, tourist_home::class.java)
                                                startActivity(intent)
                                                Toast.makeText(baseContext, "Logged in as Tourist.", Toast.LENGTH_SHORT).show()
                                            }
                                            "Hotel Owner" -> {
                                                val intent = Intent(this@login, startup::class.java)
                                                startActivity(intent)
                                                Toast.makeText(baseContext, "Logged in as Hotel Owner.", Toast.LENGTH_SHORT).show()
                                            }
                                            "Tourist Police" -> {
                                                val intent = Intent(this@login, startup::class.java)
                                                startActivity(intent)
                                                Toast.makeText(baseContext, "Logged in as Tourist Police.", Toast.LENGTH_SHORT).show()
                                            }
                                            else -> {
                                                Toast.makeText(baseContext, "Place Not Found", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    enterEmail.text.clear()
                                    enterPassword.text.clear()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle errors, if any
                            }
                        })
                    } else {
                        // If sign-in fails, display an error message
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}