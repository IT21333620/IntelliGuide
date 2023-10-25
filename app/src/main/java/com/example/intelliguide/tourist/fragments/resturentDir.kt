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
import com.example.intelliguide.models.EventsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        val snippetTextView1 = view.findViewById<TextView>(R.id.res_detail2)

        titleTextView.text = title


        // Create a reference to the "Events" table in Firebase using the "resID" as the key
        val databaseReference = FirebaseDatabase.getInstance().getReference("Events").child(snippet ?: "")

        // Attach a ValueEventListener to retrieve event data
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Retrieve data from the DataSnapshot and store it in an EventsModel
                    val eventModel = snapshot.getValue(EventsModel::class.java)

                    // Now you can use eventModel to access event data
                    val eventTitle = eventModel?.title
                    val eventDescription = eventModel?.description

                    snippetTextView.text = eventTitle
                    snippetTextView1.text = eventDescription
                } else {
                    // Data with the provided "resID" does not exist
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur during the retrieval
            }
        })

        val directionsButton = view.findViewById<Button>(R.id.resD)
        directionsButton.setOnClickListener {
            val destinationLatLng = "$latitude,$longitude"
            val uri = "http://maps.google.com/maps?daddr=$destinationLatLng"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
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
