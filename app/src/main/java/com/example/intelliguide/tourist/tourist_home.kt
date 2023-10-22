package com.example.intelliguide.tourist


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.intelliguide.R
import com.example.intelliguide.tourist.fragments.Alerts
import com.example.intelliguide.tourist.fragments.chatbot
import com.example.intelliguide.tourist.fragments.destinations
import com.example.intelliguide.tourist.fragments.profile
import com.example.intelliguide.tourist.fragments.restaurants
import com.example.intelliguide.tourist.fragments.socialMedia

class tourist_home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tourist_home)

        val imgSocialMedia:ImageView = findViewById(R.id.imgSocial);
        val imgDestinations:ImageView = findViewById(R.id.imgDestination)
        val imgRestaurants:ImageView = findViewById(R.id.imgResturant)
        val imgChatBot:ImageView = findViewById(R.id.imgChatBot)
        val imgAlert:ImageView = findViewById(R.id.imgAlert)
        val imgProfile:ImageView = findViewById(R.id.imgProfile)

        val fragmentsocialMedia = socialMedia()
        val fragmentdestinations =destinations()
        val fragmentrestaurants = restaurants()
        val fragmentChatBot = chatbot()
        val fragmementAlerts = Alerts()
        val fragmentProfile = profile()

        imgSocialMedia.setOnClickListener {
            imgSocialMedia.setImageResource(R.drawable.selected_socail_icon)
            imgDestinations.setImageResource(R.drawable.unselected_location_icon)
            imgRestaurants.setImageResource(R.drawable.unselected_food)
            imgChatBot.setImageResource(R.drawable.unselected_chat_icon)
            imgAlert.setImageResource(R.drawable.unselected_alert_icon)
            imgProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmentsocialMedia)
                commit()
            }
        }

        imgDestinations.setOnClickListener {
            imgSocialMedia.setImageResource(R.drawable.unselected_social_icon)
            imgDestinations.setImageResource(R.drawable.selected_location_icon)
            imgRestaurants.setImageResource(R.drawable.unselected_food)
            imgChatBot.setImageResource(R.drawable.unselected_chat_icon)
            imgAlert.setImageResource(R.drawable.unselected_alert_icon)
            imgProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmentdestinations)
                commit()
            }
        }

        imgRestaurants.setOnClickListener {
            imgSocialMedia.setImageResource(R.drawable.unselected_social_icon)
            imgDestinations.setImageResource(R.drawable.unselected_location_icon)
            imgRestaurants.setImageResource(R.drawable.selected_food_icon)
            imgChatBot.setImageResource(R.drawable.unselected_chat_icon)
            imgAlert.setImageResource(R.drawable.unselected_alert_icon)
            imgProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmentrestaurants)
                commit()
            }
        }

        imgChatBot.setOnClickListener {
            imgSocialMedia.setImageResource(R.drawable.unselected_social_icon)
            imgDestinations.setImageResource(R.drawable.unselected_location_icon)
            imgRestaurants.setImageResource(R.drawable.unselected_food)
            imgChatBot.setImageResource(R.drawable.selected_chat_icon)
            imgAlert.setImageResource(R.drawable.unselected_alert_icon)
            imgProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmentChatBot)
                commit()
            }
        }

        imgAlert.setOnClickListener {
            imgSocialMedia.setImageResource(R.drawable.unselected_social_icon)
            imgDestinations.setImageResource(R.drawable.unselected_location_icon)
            imgRestaurants.setImageResource(R.drawable.unselected_food)
            imgChatBot.setImageResource(R.drawable.unselected_chat_icon)
            imgAlert.setImageResource(R.drawable.selected_alert_icon)
            imgProfile.setImageResource(R.drawable.unselected_profile)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmementAlerts)
                commit()
            }
        }

        imgProfile.setOnClickListener {
            imgSocialMedia.setImageResource(R.drawable.unselected_social_icon)
            imgDestinations.setImageResource(R.drawable.unselected_location_icon)
            imgRestaurants.setImageResource(R.drawable.unselected_food)
            imgChatBot.setImageResource(R.drawable.unselected_chat_icon)
            imgAlert.setImageResource(R.drawable.unselected_alert_icon)
            imgProfile.setImageResource(R.drawable.selected_profile_icon)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2,fragmentProfile)
                commit()
            }
        }
    }
}