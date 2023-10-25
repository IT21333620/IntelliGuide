package com.example.intelliguide.tourist.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.intelliguide.R
import com.example.intelliguide.login.startup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class TouristAddPostScreen : Fragment() {

    private var imageView: ImageView? = null // Make it nullable
    private lateinit var databaseReference: DatabaseReference
    private val handler = Handler()
    private val imageUrls = ArrayList<String>()
    private var currentImageIndex = 0
    private val imageInterval = 3000 // 3 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tourist_add_post_screen, container, false)

        imageView = view?.findViewById(R.id.addPostScreenAdDisplay) // Use safe call operator

        val addPost = AddNewPlace()
        val btnAddPlace = view.findViewById<ImageView>(R.id.btnAddnewPLace)

        btnAddPlace.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, addPost)
                .commit()
        }
        
        databaseReference = FirebaseDatabase.getInstance().getReference("imageUrls")

        // Add a ValueEventListener to fetch image URLs and store them in the list.
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                imageUrls.clear()
                for (imageSnapshot in dataSnapshot.children) {
                    val imageUrl = imageSnapshot.child("imageURL").getValue(String::class.java)
                    if (imageUrl != null) {
                        imageUrls.add(imageUrl)
                    }
                }

                // Start the image rotation
                rotateImages()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here.
            }
        })

        val btnAddScam = view.findViewById<ImageView>(R.id.scmAdPst)
        btnAddScam.setOnClickListener{

            val fragment = AddScamPost()
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

    private fun rotateImages() {
        handler.postDelayed({
            if (imageUrls.isNotEmpty()) {
                Picasso.get().load(imageUrls[currentImageIndex]).into(imageView)
                currentImageIndex = (currentImageIndex + 1) % imageUrls.size
            }
            rotateImages() // Continue rotating images
        }, imageInterval.toLong())
    }
}
