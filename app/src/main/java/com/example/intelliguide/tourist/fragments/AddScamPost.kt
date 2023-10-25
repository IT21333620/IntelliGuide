package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.intelliguide.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AddScamPost : Fragment() {

    private lateinit var Heading: EditText
    private lateinit var Descrip: EditText
    private lateinit var AddScm: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_scam_post, container, false)

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().getReference("Scam")
        Heading = view.findViewById(R.id.ScmTypeIn)
        Descrip = view.findViewById(R.id.ScmTypDesIn)
        AddScm = view.findViewById(R.id.PstScamButn)

        AddScm.setOnClickListener {
            // Get the current user's UID
            val user = auth.currentUser
            val uid = user?.uid

            // Check if title and content are not empty
            val title = Heading.text.toString()
            val content = Descrip.text.toString()

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                // Create a reference to the "Scams" table in the database
                // Generate a unique key for the new entry
                val newScamKey = dbRef.push().key

                // Create a map to store the data, including the UID
                val scamData = HashMap<String, Any>()
                scamData["title"] = title
                scamData["content"] = content
                // Add the UID to the map
                scamData["uid"] = uid ?: ""

                // Store the data in the database
                dbRef.child(newScamKey!!).setValue(scamData)

                Heading.text.clear()
                Descrip.text.clear()

                // Display a success message
                Toast.makeText(context, "Scam added successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Display an error message if title or content is empty
                Toast.makeText(context, "Title and Content must not be empty", Toast.LENGTH_SHORT).show()
            }
        }


        return  view
    }

}