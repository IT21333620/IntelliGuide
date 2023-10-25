package com.example.intelliguide.hotelOwner.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.intelliguide.R
import com.example.intelliguide.login.startup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HotelProfile : Fragment() {
    private lateinit var textViewHotelOwnerName: TextView
    private lateinit var textViewHotelName: TextView
    private lateinit var textViewHotelAddress: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hotel_profile, container, false)
        textViewHotelOwnerName = view.findViewById(R.id.hotelOwnerNameTV)
        textViewHotelName = view.findViewById(R.id.hotelNameTV)
        textViewHotelAddress = view.findViewById(R.id.hotelAddressTV)
        auth = FirebaseAuth.getInstance()

        // Get the current user's UID
        val userId = auth.currentUser?.uid

        if (userId != null) {
            // Reference to the user's data in the database
            dbRef = FirebaseDatabase.getInstance().getReference("HotelOwnerModel").child(userId)

            // Retrieve the user's data and set it to the appropriate text views
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ownerName = snapshot.child("name").getValue(String::class.java)
                        val hotelName = snapshot.child("hotelName").getValue(String::class.java)
                        val hotelAddress = snapshot.child("hotelAddress").getValue(String::class.java)

                        if (ownerName != null) {
                            textViewHotelOwnerName.text = ownerName
                        }

                        if (hotelName != null) {
                            textViewHotelName.text = hotelName
                        }

                        if (hotelAddress != null) {
                            textViewHotelAddress.text = hotelAddress
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors, if any
                }
            })
        }

        val logOutBtn: ImageButton = view.findViewById(R.id.hotelOwnerButton1)

        logOutBtn.setOnClickListener {
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


}