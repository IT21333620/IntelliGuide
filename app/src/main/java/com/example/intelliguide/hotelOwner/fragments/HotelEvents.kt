package com.example.intelliguide.hotelOwner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.intelliguide.R
import android.widget.Button
import android.widget.EditText
import com.example.intelliguide.models.Event
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HotelEvents : Fragment() {

    private lateinit var myRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hotel_events, container, false)

        // Initialize Firebase Database reference
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("events")

        val submitButton = view.findViewById<Button>(R.id.hotelAddEventButton)
        submitButton.setOnClickListener {
            saveEventData(view)
        }

        return view
    }

    private fun saveEventData(view: View) {
        val organizerName = view.findViewById<EditText>(R.id.hotelAddOrganizerNameET).text.toString()
        val organizerContact = view.findViewById<EditText>(R.id.hotelAddOrganizerContactET).text.toString()
        val eventLocation = view.findViewById<EditText>(R.id.hotelEventLocationET).text.toString()
        val eventDate = view.findViewById<EditText>(R.id.hotelEventDateET).text.toString()
        val eventTime = view.findViewById<EditText>(R.id.hotelEventTimeET).text.toString()

        val event = Event(organizerName, organizerContact, eventLocation, eventDate, eventTime)

        val eventId = myRef.push().key
        eventId?.let {
            myRef.child(it).setValue(event)
        }
    }
}