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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social_media, container, false)

        // Find the image buttons
        val scamButton = view.findViewById<ImageButton>(R.id.TouristSocialScamBtn)
        val reviewButton = view.findViewById<ImageButton>(R.id.TouristSocialReviewBtn)
        val placeButton = view.findViewById<ImageButton>(R.id.TouristSocialPlaceBtn)
        val addNewPlace = view.findViewById<Button>(R.id.btnAddPost)

        // Define fragments
        val fragment1 = SocialMediaPlaces()
//        val fragment2 = SocialMediaReviews()
//        val fragment3 = SocialMediaPlaces()
//
        // Automatically load fragment1 (SocialMediaScams) into the container
        parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment1).commit()

        addNewPlace.setOnClickListener {
            val nextpage = TouristAddPostScreen()
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, nextpage)
                .commit()
        }

        scamButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment1)
                .commit()
        }
//
//        reviewButton.setOnClickListener {
//            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment2)
//                .commit()
//        }
//
//        placeButton.setOnClickListener {
//            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSM, fragment3)
//                .commit()
//        }

        return view
    }
}
