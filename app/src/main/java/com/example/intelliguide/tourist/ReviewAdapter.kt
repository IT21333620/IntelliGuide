package com.example.intelliguide.tourist

import com.example.intelliguide.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.models.Review

class ReviewAdapter(private val reviewList : ArrayList<Review>) : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.review_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = reviewList[position]

        holder.firstName.text = currentitem.placeName
        holder.lastName.text = currentitem.rating.toString()
        holder.review.text = currentitem.review

    }

    override fun getItemCount(): Int {

        return reviewList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val firstName : TextView = itemView.findViewById(R.id.SocialMediaReviewTV1)
        val lastName : TextView = itemView.findViewById(R.id.SocialMediaReviewTV3)
        val review : TextView = itemView.findViewById(R.id.SocialMediaReviewTV2)
    }

}
