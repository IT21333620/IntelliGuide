package com.example.intelliguide.hotelOwner

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.example.intelliguide.R

class AdSuccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_succes)

        val addSuccessBackBtn = findViewById<ImageButton>(R.id.addSuccessBackBtn)
        addSuccessBackBtn.setOnClickListener {
            // Create an intent to start the HotelAddAds activity
            val intent = Intent(this, HotelAddAds::class.java)
            startActivity(intent)
        }
    }
}
