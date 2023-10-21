package com.example.intelliguide.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.intelliguide.MainActivity
import com.example.intelliguide.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class startup : AppCompatActivity() {
    private lateinit var btnStart: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        btnStart = findViewById<ImageButton>(R.id.btnStart)

        btnStart.setOnClickListener{
            val intent = Intent(this@startup, login::class.java)
            startActivity(intent)
        }

    }
}