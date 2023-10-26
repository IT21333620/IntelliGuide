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
    val fragmentHotelReviews = HotelProfile()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_home)

        val fragmentHotelReviews = HotelReviews()
        val fragmentHotelMenu = HotelMenu()
        val fragmentHotelEvents = HotelEvents()
        val fragmentHotelProfile = HotelProfile()

        val imgReviews: ImageView = findViewById(R.id.imgReviews)
        val imgMenu: ImageView = findViewById(R.id.imgMenu)
        val imgEvents: ImageView = findViewById(R.id.imgEvents)
        val imgHotelProfile: ImageView = findViewById(R.id.imgHotelProfile)

        imgReviews.setOnClickListener {
            imgReviews.setImageResource(R.drawable.selected_hotel_reviews)
            imgMenu.setImageResource(R.drawable.unselected_hotel_food_menu)
            imgEvents.setImageResource(R.drawable.unselected_hotel_events)
            imgHotelProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView, fragmentHotelReviews)
                commit()
            }
        }

        imgEvents.setOnClickListener {
            imgReviews.setImageResource(R.drawable.unselected_hotel_reviews)
            imgMenu.setImageResource(R.drawable.unselected_hotel_food_menu)
            imgEvents.setImageResource(R.drawable.selected_hotel_events)
            imgHotelProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView, fragmentHotelEvents)
                commit()
            }
        }

        imgMenu.setOnClickListener {
            imgReviews.setImageResource(R.drawable.unselected_hotel_reviews)
            imgMenu.setImageResource(R.drawable.selected_hotel_food_menu)
            imgEvents.setImageResource(R.drawable.unselected_hotel_events)
            imgHotelProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView, fragmentHotelMenu)
                commit()
            }
        }

        imgHotelProfile.setOnClickListener {
            imgReviews.setImageResource(R.drawable.unselected_hotel_reviews)
            imgMenu.setImageResource(R.drawable.unselected_hotel_food_menu)
            imgEvents.setImageResource(R.drawable.unselected_hotel_events)
            imgHotelProfile.setImageResource(R.drawable.selected_profile_icon)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.hotelFragmentContainerView, fragmentHotelProfile)
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
