package com.example.intelliguide.tourist.fragments

import PlaceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.models.Review
import com.example.intelliguide.tourist.ReviewAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class SocialMediaReviews : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var reviewArrayList: ArrayList<Review>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social_media_reviews, container, false)

        userRecyclerview = view.findViewById(R.id.reviewist)
        userRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerview.setHasFixedSize(true)

        reviewArrayList = ArrayList()

        dbref = FirebaseDatabase.getInstance().getReference("reviews")

        getUserData()

        return view
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("reviews/Sri Lanka Institute of Information Technology")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    reviewArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        val place = userSnapshot.getValue(Review::class.java)
                        place?.id = userSnapshot.key
                        reviewArrayList.add(place!!)
                    }
                    userRecyclerview.adapter = ReviewAdapter(reviewArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}
