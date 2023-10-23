package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.models.Place
import com.example.intelliguide.tourist.PlaceAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class SocialMediaPlaces : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var placeArrayList: ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social_media_places, container, false)

        userRecyclerview = view.findViewById(R.id.userList)
        userRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerview.setHasFixedSize(true)

        // Initialize the placeArrayList
        placeArrayList = ArrayList()

        getUserData()

        return view
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("placeUrls")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    placeArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        val place = userSnapshot.getValue(Place::class.java)
                        placeArrayList.add(place!!)
                    }
                    userRecyclerview.adapter = PlaceAdapter(placeArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}
