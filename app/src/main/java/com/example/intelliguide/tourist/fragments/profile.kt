package com.example.intelliguide.tourist.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.intelliguide.R
import com.example.intelliguide.login.startup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.os.Handler
import android.widget.ImageView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class profile : Fragment() {
    private lateinit var textViewName: TextView
    private lateinit var textViewCountry: TextView
    private lateinit var textViewAge: TextView
    private lateinit var textViewPassportNumber: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var imageView: ImageView
    private lateinit var databaseReference: DatabaseReference
    private val handler = Handler()
    private val imageUrls = ArrayList<String>()
    private var currentImageIndex = 0
    private val imageInterval = 3000 // 3 seconds
    private  lateinit var savePlace: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        textViewName = view.findViewById(R.id.touristProfileTV2)
        textViewCountry = view.findViewById(R.id.touristProfileTV5)
        textViewAge = view.findViewById(R.id.touristProfileTV7)
        textViewPassportNumber = view.findViewById(R.id.touristProfileTV8)
        imageView = view.findViewById(R.id.profAdDisplay) // Assuming you have an ImageView in your fragment layout
        auth = FirebaseAuth.getInstance()
        savePlace = view.findViewById(R.id.touristProfBtnSaved)
        val fragment1 = SavedPlaces()


        savePlace.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, fragment1)  // Make sure R.id.fragmentContainerViewSM is correct
                .commit()
        }

        // Get the current user's UID
        val userId = auth.currentUser?.uid

        if (userId != null) {
            // Reference to the user's data in the database
            dbRef = FirebaseDatabase.getInstance().getReference("touristModel").child(userId)

            // Retrieve the user's data and set it to the appropriate text views
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val country = snapshot.child("country").getValue(String::class.java)
                        val age = snapshot.child("age").getValue(String::class.java)
                        val passportNumber = snapshot.child("passportNumber").getValue(String::class.java)

                        if (name != null) {
                            textViewName.text = name
                        }

                        if (country != null) {
                            textViewCountry.text = country
                        }

                        if (age != null) {
                            textViewAge.text = age
                        }

                        if (passportNumber != null) {
                            textViewPassportNumber.text = passportNumber
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors, if any
                }
            })
        }

        // Initialize the database reference to the "imageUrls" node.
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

        val imageButton2: ImageButton = view.findViewById(R.id.imageButton2)

        imageButton2.setOnClickListener {
            // Log out the user
            auth.signOut()

            // Show a "Logged Out" toast message
            Toast.makeText(activity, "Logged Out", Toast.LENGTH_SHORT).show()

            // Redirect to StartupActivity
            val intent = Intent(activity, startup::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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
