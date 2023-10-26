package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.adapter.LocationAdapter
import com.example.intelliguide.data.LocationInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SavedPlaces : Fragment() {

    private lateinit var savedPlacesArrayList: ArrayList<LocationInfo>
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_saved_places, container, false)

        userRecyclerView = view.findViewById(R.id.re) // Initialize userRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val btnBack = view.findViewById<View>(R.id.btnBack3)
        val profile = profile()

        btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView2, profile)
                .commit()
        }

        // Initialize savedPlacesArrayList
        savedPlacesArrayList = ArrayList()

        getUserData()

        return view
    }

    private fun getUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        val query = FirebaseDatabase.getInstance().getReference("locationTime")
            .child(userId)

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                savedPlacesArrayList.clear()
                if (snapshot.exists()) {
                    for (locationSnapshot in snapshot.children) {
                        val location = locationSnapshot.getValue(LocationInfo::class.java)
                        savedPlacesArrayList.add(location!!)
                    }


                    // Initialize and set adapter
                    locationAdapter = LocationAdapter(savedPlacesArrayList, requireContext())
                    userRecyclerView.adapter = locationAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }
}
