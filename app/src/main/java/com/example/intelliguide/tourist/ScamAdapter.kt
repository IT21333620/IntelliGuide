package com.example.intelliguide.tourist

import com.example.intelliguide.R
import com.example.intelliguide.models.Scam
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScamAdapter(private val scamList : ArrayList<Scam>) : RecyclerView.Adapter<ScamAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.scam_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = scamList[position]

        holder.firstName.text = currentitem.title
        holder.lastName.text = currentitem.content

    }

    override fun getItemCount(): Int {

        return scamList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val firstName : TextView = itemView.findViewById(R.id.SocialMediaScamTV1)
        val lastName : TextView = itemView.findViewById(R.id.SocialMediaScamTV2)

    }

}
