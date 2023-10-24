package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.intelliguide.R

class socialMedia : Fragment() {
    // Define references to the image buttons
    private lateinit var scamButton: ImageButton
    private lateinit var reviewButton: ImageButton
    private lateinit var placeButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social_media, container, false)

        // Find the image buttons
        scamButton = view.findViewById(R.id.TouristSocialScamBtn)
        reviewButton = view.findViewById(R.id.TouristSocialReviewBtn)
        placeButton = view.findViewById(R.id.TouristSocialPlaceBtn)
        val addNewPlace = view.findViewById<Button>(R.id.btnAddPost)

        // Define fragments
        val fragment1 = SocialMediaScams()
        val fragment2 = SocialMediaReviews()
        val fragment3 = SocialMediaPlaces()

        parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment1).commit()

        addNewPlace.setOnClickListener {
            val nextpage = TouristAddPostScreen()
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, nextpage).commit()
        }

        scamButton.setOnClickListener {
            // Switch the selected image for the scamButton
            scamButton.setImageResource(R.drawable.selected_tourist_socialmdeia)
            reviewButton.setImageResource(R.drawable.unselected_tourist_socialmdeia)
            placeButton.setImageResource(R.drawable.unselected_tourist_socialmdeia)

            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment1).commit()
        }

        reviewButton.setOnClickListener {
            // Switch the selected image for the reviewButton
            scamButton.setImageResource(R.drawable.unselected_tourist_socialmdeia)
            reviewButton.setImageResource(R.drawable.selected_tourist_socialmdeia)
            placeButton.setImageResource(R.drawable.unselected_tourist_socialmdeia)

            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment2).commit()
        }

        placeButton.setOnClickListener {
            // Switch the selected image for the placeButton
            scamButton.setImageResource(R.drawable.unselected_tourist_socialmdeia)
            reviewButton.setImageResource(R.drawable.unselected_tourist_socialmdeia)
            placeButton.setImageResource(R.drawable.selected_tourist_socialmdeia)

            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment3).commit()
        }

        return view
    }
}

