package com.example.intelliguide.police

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.intelliguide.R
import com.example.intelliguide.police.fragments.AlertView
import com.example.intelliguide.police.fragments.PoliceProfile

class PoliceHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_home)

        val imgTouristPoliceProfile:ImageView = findViewById(R.id.imgProfile);
        val imgAlertView:ImageView = findViewById(R.id.imgAlert)

        val fragmentpoliceProfile = PoliceProfile()
        val fragmentalertview = AlertView()

        imgTouristPoliceProfile.setOnClickListener {
            imgTouristPoliceProfile.setImageResource(R.drawable.selected_profile_icon)
            imgAlertView.setImageResource(R.drawable.unselected_alert)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmentpoliceProfile)
                commit()
            }
        }

        imgAlertView.setOnClickListener {
            imgTouristPoliceProfile.setImageResource(R.drawable.unselected_profile)
            imgAlertView.setImageResource(R.drawable.selected_alert)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmentalertview)
                commit()
            }
        }
    }
}