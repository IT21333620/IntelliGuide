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

class profile : Fragment() {
    private lateinit var textViewName: TextView
    private lateinit var textViewCountry: TextView
    private lateinit var textViewAge: TextView
    private lateinit var textViewPassportNumber: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        textViewName = view.findViewById(R.id.touristProfileTV2)
        textViewCountry = view.findViewById(R.id.touristProfileTV5)
        textViewAge = view.findViewById(R.id.touristProfileTV7)
        textViewPassportNumber = view.findViewById(R.id.touristProfileTV8)
        auth = FirebaseAuth.getInstance()

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
}
