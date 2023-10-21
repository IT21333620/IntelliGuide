package com.example.intelliguide.tourist.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.intelliguide.R
import com.example.intelliguide.models.AlertsModel
import com.example.intelliguide.models.TouristModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Alerts : Fragment() {

    private lateinit var submit: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    private lateinit var alertsRef: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationURL: String
    private val REQUEST_LOCATION_PERMISSION = 123


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_alerts, container, false)
        //enter code here

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("touristModel") // Assuming this is your touristModel table
        alertsRef = database.getReference("alertsModel")
        submit = view.findViewById(R.id.AlertIn)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())



        submit.setOnClickListener {
            // Get the currently logged-in user's UID
            Log.d("Alerts", "Submit button clicked")
            val currentUser = auth.currentUser
            val currentUserUID = currentUser?.uid

            if (currentUserUID != null) {
                // Check and request location permission if needed
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Request location updates
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            // Get the device's current location as latitude and longitude
                            location?.let {
                                val latitude = it.latitude
                                val longitude = it.longitude

                                // Generate the location URL
                                val locationURL = "https://www.google.com/maps?q=$latitude,$longitude"

                                // Query the touristModel table to get the user's data
                                usersRef.child(currentUserUID)
                                    .addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            // Check if the user's data exists
                                            if (dataSnapshot.exists()) {
                                                val userData =
                                                    dataSnapshot.getValue(TouristModel::class.java)

                                                // Now you have the user's data and the location URL, create an alertsModel and insert it
                                                if (userData != null) {
                                                    val alertsData = AlertsModel(
                                                        name = userData.name,
                                                        age = userData.age,
                                                        country = userData.country,
                                                        contact = userData.contact,
                                                        passportNumber = userData.passportNumber,
                                                        locationUrl = locationURL // Use the generated location URL
                                                    )

                                                    // Insert the alertsData into the alertsModel table
                                                    alertsRef.child(currentUserUID)
                                                        .setValue(alertsData)

                                                    // After successfully inserting data, navigate to the "StopAlert" fragment
                                                    val fragment = StopAlert()
                                                    val fragmentManager = parentFragmentManager
                                                    fragmentManager.beginTransaction()
                                                        .replace(
                                                            R.id.fragmentContainerView2,
                                                            fragment
                                                        )
                                                        .addToBackStack(null)
                                                        .commit()
                                                }
                                            }
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Handle error
                                        }
                                    })
                            }
                        }
                } else {
                    // Location permission is not granted. Request the permission.
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION
                    )
                }
            }
        }
        return view
    }
}