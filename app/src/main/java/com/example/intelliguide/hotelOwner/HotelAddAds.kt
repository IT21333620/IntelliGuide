package com.example.intelliguide.hotelOwner

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.intelliguide.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class HotelAddAds : AppCompatActivity() {

    private lateinit var chooseImageBtn: Button
    private lateinit var uploadImageBtn: Button
    private lateinit var imageView: ImageView
    private lateinit var imageNameEditText: EditText
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_add_ads)

        chooseImageBtn = findViewById(R.id.hotelAddAdButton1)
        uploadImageBtn = findViewById(R.id.hotelAddAdButton2)
        imageView = findViewById(R.id.hotelAddAdImageView)
        imageNameEditText = findViewById(R.id.hotelAddAdNameET)

        chooseImageBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Pick your image to upload"),
                22
            )
        }

        uploadImageBtn.setOnClickListener {
            uploadImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            val storageReference: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())

            storageReference.putFile(fileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        val imageFileName = imageNameEditText.text.toString() // Get the image name from the EditText

                        // Store the image URL and image name in the Realtime Database.
                        val databaseReference = FirebaseDatabase.getInstance().reference.child("imageUrls")
                        val key = databaseReference.push().key

                        if (key != null) {
                            val dataToSave = mapOf(
                                "imageURL" to imageUrl,
                                "imageName" to imageFileName
                            )
                            databaseReference.child(key).setValue(dataToSave)
                            progressDialog.dismiss()
                            Toast.makeText(applicationContext, "Image Uploaded..", Toast.LENGTH_SHORT).show()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(applicationContext, "Failed to store image URL.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Fail to Upload Image..", Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                }
        }
    }
}