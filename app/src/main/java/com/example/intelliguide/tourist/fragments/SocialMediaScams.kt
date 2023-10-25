package com.example.intelliguide.tourist.fragments

import PlaceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.models.Scam
import com.example.intelliguide.tourist.ScamAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class SocialMediaScams : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var scamArrayList: ArrayList<Scam>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social_media_scams, container, false)

        userRecyclerview = view.findViewById(R.id.scamList)
        userRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerview.setHasFixedSize(true)

        // Initialize the scamArrayList
        scamArrayList = ArrayList()

        // Update the reference to point to all scams
        dbref = FirebaseDatabase.getInstance().getReference("Scam")

        getUserData()

        return view
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("Scam")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    scamArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        val place = userSnapshot.getValue(Scam::class.java)
                        place?.id = userSnapshot.key
                        scamArrayList.add(place!!)
                    }
                    userRecyclerview.adapter = ScamAdapter(scamArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}
