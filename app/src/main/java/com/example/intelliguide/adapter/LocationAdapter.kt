package com.example.intelliguide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.data.LocationInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LocationAdapter(private var locationList: ArrayList<LocationInfo>, private val context: Context) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    interface OnSavedItemClickListener {
        fun onSavedItemClick(position: Int)
    }

    private var sListener: OnSavedItemClickListener? = null

    fun setOnSavedItemClickListener(clickListener: OnSavedItemClickListener) {
        sListener = clickListener
    }

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val savedName: TextView = itemView.findViewById(R.id.savedName)
        val savedDate: TextView = itemView.findViewById(R.id.savedDate)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemove)
        val txRemove: TextView = itemView.findViewById(R.id.txRemove)
        val rating: RatingBar = itemView.findViewById(R.id.ratingBar2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_card, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]
        holder.savedName.text = location.locationName // Assuming placeName is the attribute for place name
        holder.savedDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(location.timestamp))

        holder.rating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                // Save the rating if the user hasn't already rated the place
                saveRatingIfNotRated(context, location.locationName, rating)
            }
        }

        holder.btnRemove.setOnClickListener {
            deleteSavedLocation(position)
        }

        holder.txRemove.setOnClickListener {
            deleteSavedLocation(position)
        }

        holder.itemView.setOnClickListener {
            sListener?.onSavedItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    private fun deleteSavedLocation(position: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val locationInfo = locationList[position]

        if (uid != null) {
            val locationTimeRef = FirebaseDatabase.getInstance().reference
                .child("locationTime")
                .child(uid)
                .child(locationInfo.locationName)

            locationTimeRef.removeValue()
                .addOnSuccessListener {
                    locationList = locationList.toMutableList().apply { removeAt(position) } as ArrayList<LocationInfo>
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, locationList.size)

                    Toast.makeText(context, "Location removed successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun getItemAtPosition(position: Int): LocationInfo {
        return locationList[position]
    }

    private fun saveRatingIfNotRated(context: Context, locationName: String, rating: Float) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val ratingRef = FirebaseDatabase.getInstance().reference
                .child("ratings")
                .child(uid)
                .child(locationName)

            ratingRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User has already rated this place
                        Toast.makeText(context, "You have already rated this place", Toast.LENGTH_SHORT).show()
                    } else {
                        // User has not rated this place, save the rating
                        ratingRef.setValue(rating)
                            .addOnSuccessListener {
                                // Successfully saved the rating
                                Toast.makeText(context, "Rating saved successfully", Toast.LENGTH_SHORT).show()
                                // Update the UI if needed
                                // For example, update the rating in your RecyclerView
                            }
                            .addOnFailureListener {
                                // Failed to save the rating
                                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



}
