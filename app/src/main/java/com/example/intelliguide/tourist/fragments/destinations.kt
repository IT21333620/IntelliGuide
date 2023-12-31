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
                .position(LatLng(7.958757903076211, 80.76028543868748))
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
                .position(LatLng(7.290627160397717, 80.63470663711085))
                .title("Kandy")
                .snippet("Kandy is a city in Sri Lanka known for its cultural and historical significance. " +
                        "It's home to the Temple of the Tooth Relic, a UNESCO World Heritage site, " +
                        "and is nestled amidst lush, picturesque hills in the central part of the country. " +
                        "Kandy is a popular destination for travelers seeking a glimpse of Sri Lanka's rich heritage.")
        )

        // Galle (City)
        val galleMarker = mGoogleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(6.036941585395958, 80.21680236093829))
                .title("Galle")
                .snippet("Galle is a historic coastal city in Sri Lanka known for its well-preserved " +
                        "Dutch colonial architecture, pristine beaches, and vibrant cultural scene. " +
                        "The city's Galle Fort, a UNESCO World Heritage Site, is a major attraction," +
                        " offering a glimpse into its colonial past. " +
                        "Galle is also famous for its annual Galle Literary Festival.")
        )

        // Polonnaruwa
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.94091194414504, 81.01952886826162))
            .title("Polonnaruwa")
            .snippet("Polonnaruwa, located in Sri Lanka, is a UNESCO World Heritage " +
                    "Site known for its ancient ruins. It was the second capital of the Kingdom of " +
                    "Polonnaruwa and boasts impressive archaeological treasures, " +
                    "including well-preserved temples, statues, and royal gardens.")
        )

        // Anuradhapura
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(8.312043324359344, 80.4139518352486))
            .title("Anuradhapura")
            .snippet("Anuradhapura is an ancient city in Sri Lanka and a " +
                    "UNESCO World Heritage Site known for its historical and cultural significance. " +
                    "It was the capital of early Sri Lankan kingdoms and boasts remarkable Buddhist relics, " +
                    "including the sacred Bodhi Tree. " +
                    "Anuradhapura remains a major pilgrimage site and an archaeological treasure.")
        )

        // Dambulla Cave Temple
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.8550841428603855, 80.65059135300241))
            .title("Dambulla Cave Temple")
            .snippet("The Dambulla Cave Temple, located in Sri Lanka, is a " +
                    "UNESCO World Heritage Site known for its remarkable cave complex, " +
                    "housing stunning rock-cut Buddhist statues and murals. " +
                    "Dating back to the 1st century BC, it is one of the most significant pilgrimage sites for " +
                    "Buddhists in the country.")
        )

        // Nuwara Eliya
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.949660422003951, 80.79090215446433))
            .title("Nuwara Eliya")
            .snippet("Nuwara Eliya, located in the central highlands of " +
                    "Sri Lanka, is often referred to as \"Little England\" due to its cool climate and " +
                    "British colonial influence. It's renowned for its lush tea plantations, " +
                    "picturesque landscapes, and is a popular destination for nature lovers and tea enthusiasts.\n")
        )

        // Yala National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.491635434682699, 81.4262035818245))
            .title("Yala National Park")
            .snippet("Yala National Park, located in Sri Lanka, is renowned for its incredible biodiversity " +
                    "and is the country's most visited national park. It offers a chance to spot diverse wildlife" +
                    ", including leopards, elephants, and numerous bird species, in its varied ecosystems.")
        )

        // Udawalawe National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.474927284800055, 80.87632972415206))
            .title("Udawalawe National Park")
            .snippet("Udawalawe National Park, located in Sri Lanka, is renowned for its rich biodiversity " +
                    "and as a sanctuary for elephants. It offers a stunning landscape of grasslands, " +
                    "forests, and a large reservoir, making it a prime location for wildlife " +
                    "enthusiasts and elephant sightings.")
        )

        // Ella
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.866672332532782, 81.04640283728064))
            .title("Ella")
            .snippet("Ella is a charming town nestled in the highlands of Sri Lanka, renowned for " +
                    "its breathtaking views of lush tea plantations and picturesque landscapes. " +
                    "It's a popular destination for hiking and nature enthusiasts, with attractions " +
                    "like Ella Rock and the Nine Arch Bridge.")
        )

        // Adam's Peak (Sri Pada)
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.810580217289071, 80.499259378818))
            .title("Adam's Peak (Sri Pada)")
            .snippet("Adam's Peak, also known as Sri Pada, is a sacred mountain in Sri Lanka," +
                    " revered by multiple religions. Its iconic conical shape is famous " +
                    "for the \"Sri Pada\" or \"Sacred Footprint,\" believed to be the imprint of Buddha, " +
                    "Shiva, or Adam, depending on the faith. Pilgrims climb its steep steps to witness breathtaking sunrises.")
        )

        // Horton Plains National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.826300258703641, 80.7208964481314))
            .title("Horton Plains National Park")
            .snippet("Horton Plains National Park, located in Sri Lanka, is a protected area known " +
                    "for its stunning landscapes, including World's End, a dramatic escarpment. " +
                    "The park is home to diverse wildlife and rare species." +
                    " Visitors can explore its unique ecosystem and enjoy breathtaking vistas.")
        )

        // Bentota
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.420538723056231, 80.00614663878753))
            .title("Bentota")
            .snippet("Bentota, a coastal town in Sri Lanka, is renowned for its stunning beaches and water sports. " +
                    "Visitors flock to its palm-fringed shores for sun, sand, and vibrant marine life.")
        )

        // Mirissa
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(5.9484665619295, 80.47124853726244))
            .title("Mirissa")
            .snippet("Mirissa is a picturesque coastal town in Sri Lanka known for its stunning sandy beaches, " +
                    "crystal-clear waters, and vibrant marine life. It's a " +
                    "popular destination for sunbathing, surfing, and whale watching, " +
                    "offering a relaxed and tropical atmosphere for travelers.")
        )

        // Hikkaduwa
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.1394657871033775, 80.10727254423466))
            .title("Hikkaduwa")
            .snippet("Hikkaduwa is a coastal town in Sri Lanka known for its stunning beaches, " +
                    "vibrant coral reefs, and a popular destination" +
                    " for water sports and snorkeling. It offers a relaxed, tropical atmosphere" +
                    " with a lively beachfront nightlife.")
        )

        // Pinnawala Elephant Orphanage
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.30103540415975, 80.38885499532408))
            .title("Pinnawala Elephant Orphanage")
            .snippet("Pinnawala Elephant Orphanage is a renowned elephant sanctuary in Sri Lanka. " +
                    "Located near Kandy, it provides a home to rescued and orphaned elephants," +
                    " offering visitors a chance to observe these majestic creatures up " +
                    "close as they bathe and interact in the river.")
        )

        // Trincomalee
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(8.57638639896795, 81.23454239794889))
            .title("Trincomalee")
            .snippet("Trincomalee is a coastal city in eastern Sri Lanka known for its " +
                    "stunning natural harbor and pristine beaches. " +
                    "It's a popular destination for water sports and whale watching," +
                    " and it boasts a rich historical and cultural heritage.")
        )

        // Jaffna
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(9.661580325984245, 80.02602314193815))
            .title("Jaffna")
            .snippet("Jaffna is a historic city located in the northern part of Sri Lanka. It is renowned for " +
                    "its cultural significance, vibrant Tamil heritage, " +
                    "and beautiful coastal landscapes, including the famous Jaffna Peninsula.")
        )

        // Wilpattu National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(8.48234178695569, 80.05040055024816))
            .title("Wilpattu National Park")
            .snippet("Wilpattu National Park is a renowned wildlife sanctuary in Sri Lanka, known for " +
                    "its diverse flora and fauna. It is the largest national park in the country, offering visitors the chance to " +
                    "see a wide range of wildlife, including leopards, elephants, " +
                    "and various bird species, amidst its picturesque landscapes.")
        )

        // Galle Face Green (Colombo)
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.924240767314324, 79.84494332252767))
            .title("Galle Face Green (Colombo)")
            .snippet("Galle Face Green, located in Colombo, Sri Lanka, is a picturesque ocean-side urban park, offering " +
                    "stunning sea views and a vibrant atmosphere. It's a popular destination for leisurely walks, " +
                    "picnics, and enjoying sunsets.")
        )

        // Ravana Falls
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(6.84130504211211, 81.0547880799749))
            .title("Ravana Falls")
            .snippet("Ravana Falls is a stunning waterfall located in Sri Lanka, near Ella. " +
                    "It is known for its breathtaking 25-meter drop and the surrounding lush greenery, " +
                    "making it a popular tourist attraction." +
                    " According to legend, it is associated with the story of " +
                    "Ravana, a character from the Indian epic Ramayana.")
        )

        // Kaudulla National Park
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(8.11097340827746, 80.88584346649692))
            .title("Kaudulla National Park")
            .snippet("Kaudulla National Park is a wildlife sanctuary located in " +
                    "Sri Lanka known for its diverse range of wildlife, including elephants and various bird species. " +
                    "It is a popular safari destination offering " +
                    "stunning natural landscapes and close encounters with wildlife.")
        )

        // Negombo
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.201703371758601, 79.87541450407441))
            .title("Negombo")
            .snippet("Negombo is a coastal city in Sri Lanka known for its beautiful beaches, " +
                    "fishing industry, and vibrant local culture. " +
                    "It's located near the Bandaranaike International Airport, making it a popular entry point for travelers.")
        )

        // Polonnaruwa Vatadage
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.948183949656866, 81.00121615650382))
            .title("Polonnaruwa Vatadage")
            .snippet("The Polonnaruwa Vatadage is an ancient circular relic house situated in the " +
                    "ancient city of Polonnaruwa, Sri Lanka. This historic structure " +
                    "is renowned for its architectural significance and its role in housing sacred " +
                    "relics. It is a UNESCO World Heritage Site.")
        )

        // Dalada Maligawa (Temple of the Tooth)
        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(7.293853759214011, 80.641239164635))
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


            override fun getInfoWindow(marker: Marker): View? {
                Log.d("InfoWindow", "getInfoWindow called")

                val bundle = Bundle()
                bundle.putString("title", marker.title)
                bundle.putString("snippet", marker.snippet)
                bundle.putDouble("latitude", marker.position.latitude)
                bundle.putDouble("longitude", marker.position.longitude)

                // Create the "directions" fragment and pass the data as arguments
                val directionsFragment = Directions()
                directionsFragment.arguments = bundle

                // Navigate to the "directions" fragment
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView2, directionsFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return view
            }
        })

//        mGoogleMap?.setOnInfoWindowClickListener { marker ->
//            val bundle = Bundle()
//            bundle.putString("title", marker.title)
//            bundle.putString("snippet", marker.snippet)
//            bundle.putDouble("latitude", marker.position.latitude)
//            bundle.putDouble("longitude", marker.position.longitude)
//
//            val directionsFragment = Directions()
//            directionsFragment.arguments = bundle
//
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragmentContainerView2, directionsFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }



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