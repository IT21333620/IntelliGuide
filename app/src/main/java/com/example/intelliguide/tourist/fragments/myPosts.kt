package com.example.intelliguide.tourist.fragments

import PlaceAdapter
import YourPostsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.models.Place
import com.google.firebase.database.*

class myPosts : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var placeArrayList: ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_posts, container, false)

        userRecyclerview = view.findViewById(R.id.allList)
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
                        place?.id = userSnapshot.key
                        placeArrayList.add(place!!)
                    }
                    userRecyclerview.adapter = YourPostsAdapter(placeArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}
