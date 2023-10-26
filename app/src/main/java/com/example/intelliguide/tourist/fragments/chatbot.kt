package com.example.intelliguide.tourist.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.intelliguide.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var resturentRef: DatabaseReference
    private var userLocation: Location? = null
    private var locationInfo: LocationInfo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        databaseReference = FirebaseDatabase.getInstance().reference.child("placeUrls")
        resturentRef = FirebaseDatabase.getInstance().reference.child("hotelOwnerModel")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        checkLocationPermission()

        val btnSavePlace = view.findViewById<ImageView>(R.id.btnSavePlace)
        val btnSubmitReview = view.findViewById<Button>(R.id.btnSubmitReview)

        btnSubmitReview.setOnClickListener {
            addReview()
        }

        btnSavePlace.setOnClickListener {
            saveLocationWithTime(view.findViewById<TextView>(R.id.textView36).text.toString())
        }


        return view
    }

    private fun addReview() {
        val reviewEditText = view?.findViewById<EditText>(R.id.ETreview)
        val ratingBar = view?.findViewById<RatingBar>(R.id.ratingBar)
        val placeName = view?.findViewById<TextView>(R.id.textView36)?.text.toString()

        val review = reviewEditText?.text.toString()
        val rating = ratingBar?.rating.toString()

        if (!review.isBlank() && !rating.isBlank()) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if (uid != null) {
                val reviewRef = FirebaseDatabase.getInstance().reference.child("reviews")
                val reviewKey = reviewRef.push().key

                reviewRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child(placeName).hasChild(uid)) {
                            Toast.makeText(requireContext(), "You have already reviewed this place", Toast.LENGTH_SHORT).show()
                        } else {
                            val reviewMap = mapOf(
                                "review" to review,
                                "rating" to rating,
                                "placeName" to placeName
                            )

                            val reviewRefForPlace = reviewRef.child(placeName).child(uid)
                            reviewRefForPlace.setValue(reviewMap).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Review added successfully", Toast.LENGTH_SHORT).show()
                                    reviewEditText?.text?.clear()
                                    ratingBar?.rating = 0.0f

                                } else {
                                    Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(requireContext(), "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(requireContext(), "Please provide both a review and a rating", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLocationWithTime(locationName: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val locationTimeRef = FirebaseDatabase.getInstance().reference
            .child("locationTime")
            .child(uid.toString())

        val currentTime = System.currentTimeMillis()

        val locationTimeMap = mapOf(
            "locationName" to locationName,
            "timestamp" to currentTime,
            "placeURL" to locationInfo?.placeURL
        )

        // Check if the location with the same placeURL already exists
        locationTimeRef.orderByChild("placeURL").equalTo(locationInfo?.placeURL)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User has already saved this location
                        Toast.makeText(requireContext(), "You have already saved this location", Toast.LENGTH_SHORT).show()
                    } else {
                        // User hasn't saved this location yet, proceed to save
                        val locationKey = locationTimeRef.push().key // Generate a unique key
                        locationTimeRef.child(locationKey!!).setValue(locationTimeMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Location and time saved successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
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
        task.addOnSuccessListener { location ->
            userLocation = location

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
                            userLocation?.latitude ?: 0.0,
                            userLocation?.longitude ?: 0.0,
                            location.latitude,
                            location.longitude
                        ) <= 100
                    }



                    if (nearbyLocations.isNotEmpty()) {
                        val nearestLocation = nearbyLocations.first()
                        view?.findViewById<TextView>(R.id.textView36)?.text = nearestLocation.name
                        view?.findViewById<TextView>(R.id.textView34)?.text = nearestLocation.description
                        locationInfo = nearbyLocations.first()

                        val img = nearestLocation.placeURL

                        val imageView = view?.findViewById<ImageView>(R.id.imageView16)
                        Picasso.get().load(img).placeholder(R.drawable.logo_fotor_2023092120147)
                            .into(imageView)

                        val btnAskQuestion = view?.findViewById<ImageView>(R.id.askQuestions)
                        if (nearestLocation.Added == "User") {
                            // Disable the button
                            btnAskQuestion?.isEnabled = false
                            // Show a toast message
                            Toast.makeText(requireContext(), "Not Available", Toast.LENGTH_SHORT).show()
                        } else {
                            // Enable the button
                            btnAskQuestion?.isEnabled = true

                            btnAskQuestion?.setOnClickListener {
                                val bundle = Bundle()
                                bundle.putString("locationName", nearestLocation.name)

                                val chatFragment = chatting()
                                chatFragment.arguments = bundle

                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerView2, chatFragment) // Replace with actual container ID
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }
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
