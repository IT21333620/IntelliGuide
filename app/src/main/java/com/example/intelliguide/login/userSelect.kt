package com.example.intelliguide.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.intelliguide.R

class userSelect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_select)

        val img1: ImageView = findViewById(R.id.imageView12)
        img1.setOnClickListener {
            val intent = Intent(this@userSelect, touristReg::class.java)
            startActivity(intent)
        }

        val img2: ImageView = findViewById(R.id.imageView13)
        img2.setOnClickListener {
            val intent = Intent(this@userSelect, HotelOwnerReg::class.java)
            startActivity(intent)
        }

        val img3: ImageView = findViewById(R.id.imageView14)
        img3.setOnClickListener {
            val intent = Intent(this@userSelect, TouristPoliceReg::class.java)
            startActivity(intent)
        }
    }
}