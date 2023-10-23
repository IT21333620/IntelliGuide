package com.example.intelliguide.tourist.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.intelliguide.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class AddNewPlace : Fragment() {

    private var fileUri: Uri? = null
    private lateinit var img: ImageView
    private lateinit var name: EditText
    private lateinit var destination: EditText
    private lateinit var btnSelect: Button
    private lateinit var btnPublish: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_place, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Inflate the layout for this fragment
        name = view.findViewById(R.id.newPlaceName)
        destination = view.findViewById(R.id.newPlaceDescription)
        img = view.findViewById(R.id.newPlaceImg)
        btnSelect = view.findViewById(R.id.btnAddPost)
        btnPublish = view.findViewById(R.id.btnPublishNewPlace)


        btnSelect.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Pick your image to upload"),
                22
            )
        }
        btnPublish.setOnClickListener {
            getLocationAndUploadImage()
        }

        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                img.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getLocationAndUploadImage() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            currentLocation = location
            if (currentLocation != null) {
                uploadImage()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Unable to retrieve location. Cannot upload image.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            val storageReference: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())

            storageReference.putFile(fileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        val imageFileName = name.text.toString()
                        val description = destination.text.toString()

                        val databaseReference = FirebaseDatabase.getInstance().reference.child("placeUrls")
                        val key = databaseReference.push().key
                        val uid = FirebaseAuth.getInstance().currentUser?.uid

                        if (key != null) {
                            val latitude = currentLocation!!.latitude
                            val longitude = currentLocation!!.longitude

                            val dataToSave = mapOf(
                                "placeURL" to imageUrl,
                                "name" to imageFileName,
                                "description" to description,
                                "userId" to uid,
                                "latitude" to latitude,
                                "longitude" to longitude,
                                "Added" to "User",
                                "like" to 0
                            )
                            databaseReference.child(key).setValue(dataToSave)
                            progressDialog.dismiss()
                            Toast.makeText(requireContext(), "Image Uploaded..", Toast.LENGTH_SHORT).show()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(requireContext(), "Failed to store image URL.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Fail to Upload Image..", Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                }
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

}