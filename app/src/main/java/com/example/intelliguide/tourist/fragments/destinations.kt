package com.example.intelliguide.tourist.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
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

class destinations : Fragment(), OnMapReadyCallback {

    private var mGoogleMap:GoogleMap? = null
    private var locationManager: LocationManager? = null
    private var clickedMarker: Marker? = null
    private var mapInteractionsEnabled = true

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val LOCATION_UPDATE_INTERVAL = 60000 // Update location every 10 seconds
    private val LOCATION_UPDATE_DISTANCE = 5.0f

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Update the map with the new location
            val currentLatLng = LatLng(location.latitude, location.longitude)
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_destinations, container, false)
        //enter codes here

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        // Set the camera position to Sri Lanka
        val sriLankaLatLng = LatLng(7.8731, 80.7718) // Center of Sri Lanka
        val zoomLevel = 7.8f

        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(sriLankaLatLng, zoomLevel))
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Sigiriya
        val sigiriyaMarker = mGoogleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(7.9541, 80.7547))
                .title("Sigiriya")
                .snippet("Sigiriya, also known as the \"" +
                        "Lion Rock,\" is an ancient rock fortress in Sri Lanka, " +
                        "renowned for its stunning architecture and archaeological significance. " +
                        "Perched atop a 200-meter-high rock, it offers breathtaking views and features " +
                        "ancient frescoes and the remnants of a royal palace.")
        )

        // Kandy (City)
        val kandyMarker = mGoogleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(7.2906, 80.6337))
                .title("Kandy")
                .snippet("Kandy is a city in Sri Lanka known for its cultural and historical significance. " +
                        "It's home to the Temple of the Tooth Relic, a UNESCO World Heritage site, " +
                        "and is nestled amidst lush, picturesque hills in the central part of the country. " +
                        "Kandy is a popular destination for travelers seeking a glimpse of Sri Lanka's rich heritage.")
        )

        // Galle (City)
        val galleMarker = mGoogleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(6.0324, 80.2170))
                .title("Galle")
                .snippet("Galle is a historic coastal city in Sri Lanka known for its well-preserved " +
                        "Dutch colonial architecture, pristine beaches, and vibrant cultural scene. " +
                        "The city's Galle Fort, a UNESCO World Heritage Site, is a major attraction," +
                        " offering a glimpse into its colonial past. " +
                        "Galle is also famous for its annual Galle Literary Festival.")
        )

        // Polonnaruwa
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.9321, 81.0097))
            .title("Polonnaruwa")
            .snippet("Polonnaruwa, located in Sri Lanka, is a UNESCO World Heritage " +
                    "Site known for its ancient ruins. It was the second capital of the Kingdom of " +
                    "Polonnaruwa and boasts impressive archaeological treasures, " +
                    "including well-preserved temples, statues, and royal gardens.")
        )

        // Anuradhapura
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(8.3114, 80.4037))
            .title("Anuradhapura")
            .snippet("Anuradhapura is an ancient city in Sri Lanka and a " +
                    "UNESCO World Heritage Site known for its historical and cultural significance. " +
                    "It was the capital of early Sri Lankan kingdoms and boasts remarkable Buddhist relics, " +
                    "including the sacred Bodhi Tree. " +
                    "Anuradhapura remains a major pilgrimage site and an archaeological treasure.")
        )

        // Dambulla Cave Temple
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.8500, 80.6497))
            .title("Dambulla Cave Temple")
            .snippet("The Dambulla Cave Temple, located in Sri Lanka, is a " +
                    "UNESCO World Heritage Site known for its remarkable cave complex, " +
                    "housing stunning rock-cut Buddhist statues and murals. " +
                    "Dating back to the 1st century BC, it is one of the most significant pilgrimage sites for " +
                    "Buddhists in the country.")
        )

        // Nuwara Eliya
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.9682, 80.7830))
            .title("Nuwara Eliya")
            .snippet("Nuwara Eliya, located in the central highlands of " +
                    "Sri Lanka, is often referred to as \"Little England\" due to its cool climate and " +
                    "British colonial influence. It's renowned for its lush tea plantations, " +
                    "picturesque landscapes, and is a popular destination for nature lovers and tea enthusiasts.\n")
        )

        // Yala National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.4055, 81.2093))
            .title("Yala National Park")
            .snippet("Yala National Park, located in Sri Lanka, is renowned for its incredible biodiversity " +
                    "and is the country's most visited national park. It offers a chance to spot diverse wildlife" +
                    ", including leopards, elephants, and numerous bird species, in its varied ecosystems.")
        )

        // Udawalawe National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.4647, 80.8130))
            .title("Udawalawe National Park")
            .snippet("Udawalawe National Park, located in Sri Lanka, is renowned for its rich biodiversity " +
                    "and as a sanctuary for elephants. It offers a stunning landscape of grasslands, " +
                    "forests, and a large reservoir, making it a prime location for wildlife " +
                    "enthusiasts and elephant sightings.")
        )

        // Ella
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.8781, 81.0587))
            .title("Ella")
            .snippet("Ella is a charming town nestled in the highlands of Sri Lanka, renowned for " +
                    "its breathtaking views of lush tea plantations and picturesque landscapes. " +
                    "It's a popular destination for hiking and nature enthusiasts, with attractions " +
                    "like Ella Rock and the Nine Arch Bridge.")
        )

        // Adam's Peak (Sri Pada)
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.8095, 80.4996))
            .title("Adam's Peak (Sri Pada)")
            .snippet("Adam's Peak, also known as Sri Pada, is a sacred mountain in Sri Lanka," +
                    " revered by multiple religions. Its iconic conical shape is famous " +
                    "for the \"Sri Pada\" or \"Sacred Footprint,\" believed to be the imprint of Buddha, " +
                    "Shiva, or Adam, depending on the faith. Pilgrims climb its steep steps to witness breathtaking sunrises.")
        )

        // Horton Plains National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.8104, 80.8232))
            .title("Horton Plains National Park")
            .snippet("Horton Plains National Park, located in Sri Lanka, is a protected area known " +
                    "for its stunning landscapes, including World's End, a dramatic escarpment. " +
                    "The park is home to diverse wildlife and rare species." +
                    " Visitors can explore its unique ecosystem and enjoy breathtaking vistas.")
        )

        // Bentota
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.4254, 79.9982))
            .title("Bentota")
            .snippet("Bentota, a coastal town in Sri Lanka, is renowned for its stunning beaches and water sports. " +
                    "Visitors flock to its palm-fringed shores for sun, sand, and vibrant marine life.")
        )

        // Mirissa
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(5.9444, 80.4578))
            .title("Mirissa")
            .snippet("Mirissa is a picturesque coastal town in Sri Lanka known for its stunning sandy beaches, " +
                    "crystal-clear waters, and vibrant marine life. It's a " +
                    "popular destination for sunbathing, surfing, and whale watching, " +
                    "offering a relaxed and tropical atmosphere for travelers.")
        )

        // Hikkaduwa
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.1391, 80.1020))
            .title("Hikkaduwa")
            .snippet("Hikkaduwa is a coastal town in Sri Lanka known for its stunning beaches, " +
                    "vibrant coral reefs, and a popular destination" +
                    " for water sports and snorkeling. It offers a relaxed, tropical atmosphere" +
                    " with a lively beachfront nightlife.")
        )

        // Pinnawala Elephant Orphanage
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.2939, 80.3855))
            .title("Pinnawala Elephant Orphanage")
            .snippet("Pinnawala Elephant Orphanage is a renowned elephant sanctuary in Sri Lanka. " +
                    "Located near Kandy, it provides a home to rescued and orphaned elephants," +
                    " offering visitors a chance to observe these majestic creatures up " +
                    "close as they bathe and interact in the river.")
        )

        // Trincomalee
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(8.5871, 81.2152))
            .title("Trincomalee")
            .snippet("Trincomalee is a coastal city in eastern Sri Lanka known for its " +
                    "stunning natural harbor and pristine beaches. " +
                    "It's a popular destination for water sports and whale watching," +
                    " and it boasts a rich historical and cultural heritage.")
        )

        // Jaffna
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(9.6656, 80.0057))
            .title("Jaffna")
            .snippet("Jaffna is a historic city located in the northern part of Sri Lanka. It is renowned for " +
                    "its cultural significance, vibrant Tamil heritage, " +
                    "and beautiful coastal landscapes, including the famous Jaffna Peninsula.")
        )

        // Wilpattu National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(8.3822, 80.0482))
            .title("Wilpattu National Park")
            .snippet("Wilpattu National Park is a renowned wildlife sanctuary in Sri Lanka, known for " +
                    "its diverse flora and fauna. It is the largest national park in the country, offering visitors the chance to " +
                    "see a wide range of wildlife, including leopards, elephants, " +
                    "and various bird species, amidst its picturesque landscapes.")
        )

        // Galle Face Green (Colombo)
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.9340, 79.8463))
            .title("Galle Face Green (Colombo)")
            .snippet("Galle Face Green, located in Colombo, Sri Lanka, is a picturesque ocean-side urban park, offering " +
                    "stunning sea views and a vibrant atmosphere. It's a popular destination for leisurely walks, " +
                    "picnics, and enjoying sunsets.")
        )

        // Ravana Falls
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.8234, 81.0263))
            .title("Ravana Falls")
            .snippet("Ravana Falls is a stunning waterfall located in Sri Lanka, near Ella. " +
                    "It is known for its breathtaking 25-meter drop and the surrounding lush greenery, " +
                    "making it a popular tourist attraction." +
                    " According to legend, it is associated with the story of " +
                    "Ravana, a character from the Indian epic Ramayana.")
        )

        // Kaudulla National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.8686, 80.9503))
            .title("Kaudulla National Park")
            .snippet("Kaudulla National Park is a wildlife sanctuary located in " +
                    "Sri Lanka known for its diverse range of wildlife, including elephants and various bird species. " +
                    "It is a popular safari destination offering " +
                    "stunning natural landscapes and close encounters with wildlife.")
        )

        // Negombo
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.2083, 79.8398))
            .title("Negombo")
            .snippet("Negombo is a coastal city in Sri Lanka known for its beautiful beaches, " +
                    "fishing industry, and vibrant local culture. " +
                    "It's located near the Bandaranaike International Airport, making it a popular entry point for travelers.")
        )

        // Polonnaruwa Vatadage
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.9273, 81.0086))
            .title("Polonnaruwa Vatadage")
            .snippet("The Polonnaruwa Vatadage is an ancient circular relic house situated in the " +
                    "ancient city of Polonnaruwa, Sri Lanka. This historic structure " +
                    "is renowned for its architectural significance and its role in housing sacred " +
                    "relics. It is a UNESCO World Heritage Site.")
        )

        // Dalada Maligawa (Temple of the Tooth)
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.2936, 80.6413))
            .title("Dalada Maligawa")
            .snippet("The Dalada Maligawa, also known as the Temple of the Tooth, " +
                    "is a sacred Buddhist temple in Kandy, Sri Lanka. It houses a relic believed to be " +
                    "the tooth of Buddha, making it a significant pilgrimage site. The temple complex is a " +
                    "UNESCO World Heritage Site and a symbol of cultural and religious importance in Sri Lanka.")
        )


        mGoogleMap?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker): View? {
                return null
            }

            @SuppressLint("MissingInflatedId")
            override fun getInfoWindow(marker: Marker): View? {
                Log.d("InfoWindow", "getInfoWindow called")
                val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_info, null)

                // Find and set the title, description, and button content
                val cardView = view.findViewById<CardView>(R.id.custom_card)
                val titleTextView = view.findViewById<TextView>(R.id.info_window_title)
                val descriptionTextView = view.findViewById<TextView>(R.id.info_window_description)


                // Customize the content based on the marker data
                titleTextView.text = marker?.title
                descriptionTextView.text = marker?.snippet

                cardView.setOnClickListener {
                    Log.d("Directions", "Button clicked")
                    mapInteractionsEnabled = false
                    val destinationLatLng = marker.position
                    val uri = "http://maps.google.com/maps?daddr=${destinationLatLng.latitude},${destinationLatLng.longitude}"
                    Log.d("Directions", "URI: $uri") // Log the URI to check if it's correct

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

                    if (intent.resolveActivity(requireContext().packageManager) != null) {
                        Log.d("Directions", "Google Maps is available on the device.")
                        startActivity(intent)
                    } else {
                        Log.d("Directions", "Google Maps is not available on the device.")
                        // Open in a web browser
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=${destinationLatLng.latitude},${destinationLatLng.longitude}")
                        )
                        startActivity(webIntent)
                    }
                }

                return view
            }
        })


        // Check for location permission
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
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop location updates when the fragment is destroyed
        locationManager?.removeUpdates(locationListener)
    }

}