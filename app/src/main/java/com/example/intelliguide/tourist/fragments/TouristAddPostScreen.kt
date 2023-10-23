package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.intelliguide.R

class TouristAddPostScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_tourist_add_post_screen, container, false)

        val addPost = AddNewPlace()
        val btnAddPlace = view.findViewById<ImageView>(R.id.btnAddnewPLace)

        btnAddPlace.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,addPost)
                .commit()
        }
        return view
    }

}