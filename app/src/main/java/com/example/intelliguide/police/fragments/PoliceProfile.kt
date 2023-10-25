package com.example.intelliguide.police.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class PoliceProfile : Fragment() {

    private lateinit var textViewNameTitle: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewAllocatedStation: TextView
    private lateinit var textViewPoliceID: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_police_profile, container, false)
        textViewNameTitle = view.findViewById(R.id.touristPoliceNameTV1)
        textViewName = view.findViewById(R.id.touristPoliceTV2)
        textViewPoliceID = view.findViewById(R.id.touristPoliceTV3)
        textViewAllocatedStation = view.findViewById(R.id.touristPoliceTV4)
        auth = FirebaseAuth.getInstance()

        // Get the current user's UID
        val userId = auth.currentUser?.uid

        if (userId != null) {
            // Reference to the user's data in the database
            dbRef = FirebaseDatabase.getInstance().getReference("PoliceModel").child(userId)

            // Retrieve the user's data and set it to the appropriate text views
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nameTitle = snapshot.child("name").getValue(String::class.java)
                        val name = snapshot.child("name").getValue(String::class.java)
                        val policeID = snapshot.child("policeId").getValue(String::class.java)
                        val allocatedStation = snapshot.child("allocatedStation").getValue(String::class.java)

                        if (name != null) {
                            textViewNameTitle.text = nameTitle
                        }

                        if (name != null) {
                            textViewName.text = name
                        }

                        if (policeID != null) {
                            textViewPoliceID.text = policeID
                        }

                        if (allocatedStation != null) {
                            textViewAllocatedStation.text = allocatedStation
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors, if any
                }
            })
        }

        val logOutBtn: ImageButton = view.findViewById(R.id.touristPoliceRegButton1)

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