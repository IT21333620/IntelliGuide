package com.example.intelliguide.tourist.fragments

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
import com.squareup.picasso.Picasso

class chatbot : Fragment() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var hasRated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)
        val chat = chatting()

        checkLocationPermission()

        val btnAskQuestion = view.findViewById<ImageView>(R.id.askQuestions)

        btnAskQuestion.setOnClickListener {

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
            // Define a list of locations with their coordinates and names
            val locations = listOf(
                LocationInfo(
                    "Location A",
                    6.9264449,
                    79.9727329,
                    "Description A",
                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.adventuresofjellie.com%2Fsri-lanka%2Fsigiriya&psig=AOvVaw1sxeHWW_13NsO8ujV7jF31&ust=1698039386351000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCJig3OT3iIIDFQAAAAAdAAAAABAE"
                ),
                LocationInfo(
                    "Location B",
                    34.0522,
                    -118.2437,
                    "Description B",
                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.history.com%2Ftopics%2Feuropean-history%2Flondon-england&psig=AOvVaw3Wvu9XnKeAK49zyObcfAZz&ust=1698039419500000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCMjLwvT3iIIDFQAAAAAdAAAAABAE"
                )
                // Add more locations as needed
            )

            // Check if user is within 100m of any location
            val nearbyLocations = locations.filter { location ->
                calculateDistance(
                    userLocation.latitude,
                    userLocation.longitude,
                    location.latitude,
                    location.longitude
                ) <= 100
            }

            // Display name and description
            if (nearbyLocations.isNotEmpty()) {
                val nearestLocation = nearbyLocations.first()
                view?.findViewById<TextView>(R.id.textView36)?.text = nearestLocation.name
                view?.findViewById<TextView>(R.id.textView34)?.text = nearestLocation.description

                // Set image
                val imageView = view?.findViewById<ImageView>(R.id.imageView16)
                Picasso.get()
                    .load(nearestLocation.imageUrl)
                    .placeholder(R.drawable.logo_fotor_2023092120147) // Replace with your placeholder image
                    .into(imageView)



            }
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
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val description: String,
        val imageUrl: String,
    )
}