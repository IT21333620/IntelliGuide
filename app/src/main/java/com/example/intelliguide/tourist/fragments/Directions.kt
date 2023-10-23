package com.example.intelliguide.tourist.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.intelliguide.R
class Directions : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_directions, container, false)
        val title = arguments?.getString("title")
        val snippet = arguments?.getString("snippet")
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        // Find the TextViews in your layout
        val titleTextView = view.findViewById<TextView>(R.id.window_title)
        val snippetTextView = view.findViewById<TextView>(R.id.window_des)

        // Set the captured data to your TextViews
        titleTextView.text = title
        snippetTextView.text = snippet

        val directionsButton = view.findViewById<Button>(R.id.directions_btn)

        // Set an onClickListener to open directions
        directionsButton.setOnClickListener {
            val destinationLatLng = "$latitude,$longitude"
            val uri = "http://maps.google.com/maps?daddr=$destinationLatLng"

            // Create an intent to open the maps application
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

            // Check if a maps application is available on the device
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                // If no maps application is available, open in a web browser
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=$destinationLatLng")
                )
                startActivity(webIntent)
            }
        }

        val backButton = view.findViewById<ImageView>(R.id.bck_btn)

        backButton.setOnClickListener{
            val fragment = destinations()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainerView2,
                    fragment
                )
                .addToBackStack(null)
                .commit()
        }
        return view
    }


}