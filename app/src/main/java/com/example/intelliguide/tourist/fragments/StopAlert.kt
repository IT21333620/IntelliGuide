package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.intelliguide.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StopAlert : Fragment() {

    private lateinit var submit: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var alertsRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stop_alert, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        alertsRef = database.getReference("alertsModel")
        submit = view.findViewById(R.id.AlertDel)

        submit.setOnClickListener {
            // Get the currently logged-in user's UID
            val currentUser = auth.currentUser
            val currentUserUID = currentUser?.uid

            if (currentUserUID != null) {
                // Reference the user's entry in the alertsModel
                val userAlertsRef = alertsRef.child(currentUserUID)

                // Check if the entry exists
                userAlertsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The entry exists, delete it
                            userAlertsRef.removeValue()
                            Toast.makeText(requireContext(), "Alert Stopped", Toast.LENGTH_SHORT).show()
                            val fragment = Alerts()
                            val fragmentManager = parentFragmentManager
                            fragmentManager.beginTransaction()
                                .replace(
                                    R.id.fragmentContainerView2,
                                    fragment
                                )
                                .addToBackStack(null)
                                .commit()
                        } else {
                            Toast.makeText(requireContext(), "No entries to delete", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
            }
        }

        return view
    }


}