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
class resturentDir : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_resturent_dir, container, false)
        val title = arguments?.getString("title")
        val snippet = arguments?.getString("resID")
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        val titleTextView = view.findViewById<TextView>(R.id.res_title)
        val snippetTextView = view.findViewById<TextView>(R.id.res_detail)

        titleTextView.text = title
        snippetTextView.text = snippet

        val directionsButton = view.findViewById<Button>(R.id.resD)
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

        val backButton = view.findViewById<ImageView>(R.id.resBK)

        backButton.setOnClickListener{
            val fragment = restaurants()
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