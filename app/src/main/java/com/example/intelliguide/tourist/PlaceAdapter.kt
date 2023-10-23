package com.example.intelliguide.tourist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.models.Place
import com.squareup.picasso.Picasso

class PlaceAdapter(private val placeList: ArrayList<Place>) : RecyclerView.Adapter<PlaceAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.place_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = placeList[position]

        holder.firstName.text = currentitem.name

        // Load and display the image using Picasso or Glide
        Picasso.get().load(currentitem.placeURL).into(holder.placeImage)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.SocialMediaPlaceTV1)
        val placeImage: ImageView = itemView.findViewById(R.id.SocialMediaPlaceIV) // Assuming you have an ImageView with this ID in your place_item.xml
    }
}
