package com.example.intelliguide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.data.LocationInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LocationAdapter(private val locationList: ArrayList<LocationInfo>, private val context: Context) :
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
    }

    override fun getItemCount(): Int {
        return locationList.size
    }
}
