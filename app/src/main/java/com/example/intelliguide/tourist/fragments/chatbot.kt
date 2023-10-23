package com.example.intelliguide.tourist.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.intelliguide.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class chatbot : Fragment() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var hasRated = false
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        databaseReference = FirebaseDatabase.getInstance().reference.child("placeUrls")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)
        val chat = chatting()
        val addPlace = AddNewPlace()

        checkLocationPermission()

        val btnAskQuestion = view.findViewById<ImageView>(R.id.askQuestions)
        val btnSavePlace = view.findViewById<ImageView>(R.id.btnSavePlace)
        btnAskQuestion.setOnClickListener {

        }
        btnSavePlace.setOnClickListener {

        }


        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            if (!hasRated) {
                // Handle rating change
                // For example, you can send the rating to Firebase or perform any other action
                Toast.makeText(requireContext(), "Rating: $rating", Toast.LENGTH_SHORT).show()

                // Disable the RatingBar
                ratingBar.isEnabled = false

                // Update the flag
                hasRated = true
            } else {
                // If the user has already rated, show a message or handle it accordingly
                Toast.makeText(requireContext(), "You've already rated this location", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun checkLocationPermission() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener { userLocation ->
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val locations = mutableListOf<LocationInfo>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(LocationInfo::class.java)
                        if (place != null) {
                            locations.add(place)
                        }
                    }

                    val nearbyLocations = locations.filter { location ->
                        calculateDistance(
                            userLocation.latitude,
                            userLocation.longitude,
                            location.latitude,
                            location.longitude
                        ) <= 100
                    }

                    if (nearbyLocations.isNotEmpty()) {
                        val nearestLocation = nearbyLocations.first()
                        view?.findViewById<TextView>(R.id.textView36)?.text = nearestLocation.name
                        view?.findViewById<TextView>(R.id.textView34)?.text = nearestLocation.description

                        val img = nearestLocation.placeURL


                        val imageView = view?.findViewById<ImageView>(R.id.imageView16)
                        Picasso.get().load(img).placeholder(R.drawable.logo_fotor_2023092120147)
                            .into(imageView)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

    }

    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val R = 6371 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c * 1000 // Convert to meters
    }

    data class LocationInfo(
        val name: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val description: String = "",
        val placeURL: String = "",
        val Added: String = ""
    )

}