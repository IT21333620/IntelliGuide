package com.example.intelliguide.tourist.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.intelliguide.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class restaurants : Fragment(), OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null
    private var locationManager: LocationManager? = null
    private var userLocation: Location? = null

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val LOCATION_UPDATE_INTERVAL = 60000 // Update location every 60 seconds
    private val LOCATION_UPDATE_DISTANCE = 5.0f
    private val FILTER_RADIUS_KM = 2.0 // Set the desired radius in kilometers


    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            userLocation = location
            // Update the map with the new location
            val currentLatLng = LatLng(location.latitude, location.longitude)
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng))
            fetchHotelDataFromRealtimeDatabase(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restaurants, container, false)

        val resmap = childFragmentManager.findFragmentById(R.id.mapFragment1) as SupportMapFragment
        resmap.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Enable the "My Location" layer on the map
            mGoogleMap?.isMyLocationEnabled = true
            locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_UPDATE_INTERVAL.toLong(),
                LOCATION_UPDATE_DISTANCE,
                locationListener
            )

            // Get the last known location (current location)
            userLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (userLocation != null) {
                // Move the camera to the user's last known location
                val userLatLng = LatLng(userLocation!!.latitude, userLocation!!.longitude)
                val zoomLevel = 12.0f // You can adjust the zoom level as needed
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, zoomLevel))
                fetchHotelDataFromRealtimeDatabase(userLocation)
            } else {
                // If there's no last known location, you can handle it as needed
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        mGoogleMap?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker): View? {
                return null
            }


            override fun getInfoWindow(marker: Marker): View? {
                Log.d("InfoWindow", "getInfoWindow called")

                val bundle = Bundle()
                bundle.putString("title", marker.title)
                bundle.putString("resID", marker.snippet)
                bundle.putDouble("latitude", marker.position.latitude)
                bundle.putDouble("longitude", marker.position.longitude)

                // Create the "directions" fragment and pass the data as arguments
                val resturentFragment = resturentDir()
                resturentFragment.arguments = bundle

                // Navigate to the "directions" fragment
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView2, resturentFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return view
            }
        })
    }

    private fun isMarkerWithinRadius(userLocation: Location, markerLocation: Location): Boolean {
        val distance = userLocation.distanceTo(markerLocation)
        val radiusInMeters = FILTER_RADIUS_KM * 1000 // Convert the radius to meters
        return distance <= radiusInMeters
    }

    private fun fetchHotelDataFromRealtimeDatabase(userLocation: Location?) {
        if (userLocation != null) {
            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("hotelOwnerModel")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val hotelName = childSnapshot.child("hotelName").value.toString()
                        val latitudeStr = childSnapshot.child("latitude").value.toString()
                        val longitudeStr = childSnapshot.child("longitude").value.toString()
                        val resid = childSnapshot.key

                        try {
                            val latitude = latitudeStr.toDouble()
                            val longitude = longitudeStr.toDouble()

                            val hotelLatLng = LatLng(latitude, longitude)

                            val markerLocation = Location("hotel")
                            markerLocation.latitude = latitude
                            markerLocation.longitude = longitude

                            if (isMarkerWithinRadius(userLocation, markerLocation)) {
                                val markerOptions = MarkerOptions()
                                    .position(hotelLatLng)
                                    .title(hotelName)
                                    .snippet(resid)
                                mGoogleMap?.addMarker(markerOptions)
                            }
                        } catch (e: NumberFormatException) {
                            Log.e("Firebase", "Error converting latitude or longitude to Double", e)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Firebase", "Error getting hotel data", databaseError.toException())
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop location updates when the fragment is destroyed
        locationManager?.removeUpdates(locationListener)
    }
}
