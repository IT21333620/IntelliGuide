package com.example.intelliguide.hotelOwner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.intelliguide.R
import com.example.intelliguide.models.Payment

class HotelAdPayment : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var cardNumberET: EditText
    private lateinit var expDateET: EditText
    private lateinit var cvvET: EditText
    private lateinit var payBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_ad_payment)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference
        cardNumberET = findViewById(R.id.hotelAdPaymentET1)
        expDateET =  findViewById(R.id.hotelAdPaymentET2)
        cvvET =  findViewById(R.id.hotelAdPaymentET3)
        payBtn = findViewById(R.id.hotelAdPaymentButton)

        // Set a click listener on the button
        payBtn.setOnClickListener {
            savePaymentDetails()
        }
    }

    private fun savePaymentDetails() {
        // Get the data from the EditText fields
        val cardNumber = cardNumberET.text.toString()
        val expiryDate = expDateET.text.toString()
        val cvv = cvvET.text.toString()

        // Validate the card number
        if (cardNumber.length != 16) {
            cardNumberET.error = "Card number must be exactly 16 digits"
            return
        }

        // Validate the CVV
        if (cvv.length != 3) {
            cvvET.error = "CVV must be exactly 3 digits"
            return
        }

        // Validate the expiry date
        val currentDate = "10/23"
        if (expiryDate.length != 5 || expiryDate < currentDate) {
            expDateET.error = "Expiry date must be after $currentDate"
            return
        }

        // Extract the month part from the expiry date
        val monthStr = expiryDate.substring(0, 2)
        val month = monthStr.toIntOrNull()

        // Check if the month is within the valid range (1 to 12)
        if (month == null || month <= 0 || month > 12) {
            expDateET.error = "Invalid month in the expiry date"
            return
        }

        // Create a Payment object
        val payment = Payment(cardNumber, expiryDate, cvv)

        // Save the Payment object to Firebase Database
        databaseReference.child("Payments").push().setValue(payment)
            .addOnSuccessListener {
                // Data saved successfully, navigate to AdSuccess activity
                val intent = Intent(this, AdSuccess::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                // Handle the error, e.g., show an error message
            }
    }


}


