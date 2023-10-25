package com.example.intelliguide.hotelOwner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.intelliguide.R
import com.example.intelliguide.hotelOwner.fragments.HotelEvents
import com.example.intelliguide.hotelOwner.fragments.HotelMenu
import com.example.intelliguide.hotelOwner.fragments.HotelProfile
import com.example.intelliguide.hotelOwner.fragments.HotelReviews

class HotelHome : AppCompatActivity() {
    val fragmentHotelReviews = HotelReviews()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_home)

        val imghotelReviews: ImageView = findViewById(R.id.imgReviews);
        val imgEvents: ImageView = findViewById(R.id.imgEvents)
        val imgMenu:ImageView = findViewById(R.id.imgMenu);
        val imghotelOwnerProfile:ImageView = findViewById(R.id.imgHotelProfile)

        val fragmentReviews = HotelReviews()
        val fragmentEvents = HotelEvents()
        val fragmentMenu = HotelMenu()
        val fragmentHotelProfile = HotelProfile()

        imghotelReviews.setOnClickListener {
            imghotelReviews.setImageResource(R.drawable.selected_hotel_reviews)
            imgEvents.setImageResource(R.drawable.unselected_hotel_events)
            imgMenu.setImageResource(R.drawable.unselected_hotel_food_menu)
            imghotelOwnerProfile.setImageResource(R.drawable.baseline_person_24)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView,fragmentReviews)
                commit()
            }
        }

        imgEvents.setOnClickListener {
            imghotelReviews.setImageResource(R.drawable.unselected_hotel_reviews)
            imgEvents.setImageResource(R.drawable.unselected_hotel_events)
            imgMenu.setImageResource(R.drawable.unselected_hotel_food_menu)
            imghotelOwnerProfile.setImageResource(R.drawable.baseline_person_24)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView,fragmentEvents)
                commit()
            }
        }

        imgMenu.setOnClickListener {
            imghotelReviews.setImageResource(R.drawable.unselected_hotel_reviews)
            imgEvents.setImageResource(R.drawable.unselected_hotel_events)
            imgMenu.setImageResource(R.drawable.unselected_hotel_food_menu)
            imghotelOwnerProfile.setImageResource(R.drawable.baseline_person_24)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView,fragmentMenu)
                commit()
            }
        }

        imghotelOwnerProfile.setOnClickListener {
            imghotelReviews.setImageResource(R.drawable.unselected_hotel_reviews)
            imgEvents.setImageResource(R.drawable.unselected_hotel_events)
            imgMenu.setImageResource(R.drawable.unselected_hotel_food_menu)
            imghotelOwnerProfile.setImageResource(R.drawable.selected_profile_icon)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView,fragmentHotelProfile)
                commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Automatically load fragmentHotelReviews when the activity resumes
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.hotelFragmentContainerView, fragmentHotelReviews)
            commit()
        }
    }
}