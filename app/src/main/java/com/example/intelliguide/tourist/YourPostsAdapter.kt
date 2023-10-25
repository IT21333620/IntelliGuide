import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.models.Place
import com.squareup.picasso.Picasso
import com.google.firebase.database.FirebaseDatabase

class YourPostsAdapter(private val placeList: ArrayList<Place>) : RecyclerView.Adapter<YourPostsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.your_place_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = placeList[position]

        holder.name.text = currentitem.name
        holder.description.text = currentitem.description

        val likeCount = currentitem.like ?: 0
        holder.like.text = likeCount.toString()

        // Load and display the image using Picasso or Glide
        Picasso.get().load(currentitem.placeURL).into(holder.placeImage)

        // Add an OnClickListener to the imageView18
        holder.imageView18.setOnClickListener {
            // Increment the like count
            val updatedLikeCount = likeCount + 1

            // Update the Firebase database
            updateLikesCountInFirebase(currentitem.id, updatedLikeCount)

            // Update the TextView with the new like count
            holder.like.text = updatedLikeCount.toString()
        }

        // Add an OnClickListener to the "tourist_post_delete" ImageView
        holder.touristPostDelete.setOnClickListener {
            // Call a function to delete the selected record from the database
            deleteRecordFromFirebase(currentitem.id)
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.SocialMediaPlaceTV1)
        val placeImage: ImageView = itemView.findViewById(R.id.SocialMediaPlaceIV)
        val description: TextView = itemView.findViewById(R.id.SocialMediaPlaceTV2)
        val like: TextView = itemView.findViewById(R.id.SocialMediaPlaceTV3)
        val imageView18: ImageView = itemView.findViewById(R.id.imageView18)
        val touristPostDelete: ImageView = itemView.findViewById(R.id.tourist_post_delete)
    }

    private fun updateLikesCountInFirebase(placeId: String?, newLikeCount: Int) {
        if (placeId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference
            val placeRef = databaseReference.child("placeUrls").child(placeId)
            placeRef.child("like").setValue(newLikeCount)
        }
    }

    private fun deleteRecordFromFirebase(placeId: String?) {
        if (placeId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference
            val placeRef = databaseReference.child("placeUrls").child(placeId)

            // Remove the specific place from the database
            placeRef.removeValue()
        }
    }
}