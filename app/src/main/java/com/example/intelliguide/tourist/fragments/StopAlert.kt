package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var remainingTimeTextView: TextView
    private var buttonEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stop_alert, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        alertsRef = database.getReference("alertsModel")
        submit = view.findViewById(R.id.AlertDel)
        remainingTimeTextView = view.findViewById(R.id.remainingTimeTextView)

        // Initially visible and counting down to 30 seconds
        remainingTimeTextView.text = "Button will be disabled in 30 seconds"

        submit.isEnabled = true

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            buttonEnabled = false
            submit.isEnabled = false
            remainingTimeTextView.visibility = View.GONE // Hide the countdown text
        }, 30000) // 30 seconds

        // Update the remaining time every second
        val countdownHandler = Handler(Looper.getMainLooper())
        val remainingTimeMillis = 30000 // 30 seconds
        val countdownIntervalMillis = 1000 // 1 second
        var remainingTime = remainingTimeMillis

        countdownHandler.post(object : Runnable {
            override fun run() {
                if (remainingTime > 0) {
                    remainingTime -= countdownIntervalMillis
                    remainingTimeTextView.text = "Button will be disabled in ${remainingTime / 1000} seconds"
                    countdownHandler.postDelayed(this, countdownIntervalMillis.toLong())
                } else {
                    remainingTimeTextView.visibility = View.GONE // Hide the countdown text
                }
            }
        })

        submit.setOnClickListener {
            if (buttonEnabled) {
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
        }

        return view
    }
}