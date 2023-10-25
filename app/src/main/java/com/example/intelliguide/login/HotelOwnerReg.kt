package com.example.intelliguide.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.intelliguide.R
import com.example.intelliguide.hotelOwner.HotelHome
import com.example.intelliguide.models.HotelOwnerModel
import com.example.intelliguide.models.UserModel
import com.example.intelliguide.tourist.tourist_home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HotelOwnerReg : AppCompatActivity() {

    private lateinit var nameET: EditText
    private lateinit var ageET: EditText
    private lateinit var hotelNameET: EditText
    private lateinit var hotelAddressET: EditText
    private lateinit var longitudeET: EditText
    private lateinit var latitudeET: EditText

    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var confirmPasswordET: EditText

    private lateinit var register: Button
    private lateinit var reSignin: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_owner_reg)

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().getReference("userModel")
        dbRef2 = FirebaseDatabase.getInstance().getReference("hotelOwnerModel")

        nameET = findViewById(R.id.hotelOwnerRegEditText1)
        ageET =  findViewById(R.id.hotelOwnerRegEditText2)
        hotelNameET =  findViewById(R.id.hotelOwnerRegEditText3)
        hotelAddressET =  findViewById(R.id.hotelOwnerRegEditText5)
        longitudeET =  findViewById(R.id.hotelOwnerRegLongitudeET)
        latitudeET =  findViewById(R.id.hotelOwnerRegLatitudeET)
        emailET =  findViewById(R.id.hotelOwnerRegEditText6)
        passwordET =  findViewById(R.id.hotelOwnerRegEditText7)
        confirmPasswordET =  findViewById(R.id.hotelOwnerRegEditText8)
        register = findViewById(R.id.hotelOwnerRegButton1)
        reSignin = findViewById(R.id.hotelOwnerRegButton2)

        reSignin.setOnClickListener {
            val intent = Intent(this@HotelOwnerReg, login::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {

            var name: String = ""
            var age: String = ""
            var hotelName: String = ""
            var hotelAddress: String = ""
            var longitude: String = ""
            var latitude: String = ""
            var email: String = ""
            var password: String = ""
            var rePassword: String = ""

            name = nameET.text.toString()
            age = ageET.text.toString()
            hotelName = hotelNameET.text.toString()
            hotelAddress = hotelAddressET.text.toString()
            longitude = longitudeET.text.toString()
            latitude = latitudeET.text.toString()
            email = emailET.text.toString()
            password = passwordET.text.toString()
            rePassword = confirmPasswordET.text.toString()

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this@HotelOwnerReg, "Enter Email", Toast.LENGTH_SHORT).show()
            }

            else if (!email.contains("@")) {
                Toast.makeText(this@HotelOwnerReg, "Invalid email format", Toast.LENGTH_SHORT).show()
            }

            else if(TextUtils.isEmpty(name)){
                Toast.makeText(this@HotelOwnerReg, "Enter Name", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(age)){
                Toast.makeText(this@HotelOwnerReg, "Enter Age", Toast.LENGTH_SHORT).show()
            }
            else if(!age.matches(Regex("^[0-9.+-]+$"))) {
                Toast.makeText(this@HotelOwnerReg, "Invalid Age Value", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(hotelName)){
                Toast.makeText(this@HotelOwnerReg, "Enter Hotel Name", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(hotelAddress)){
                Toast.makeText(this@HotelOwnerReg, "Enter Hotel Address", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(longitude)){
                Toast.makeText(this@HotelOwnerReg, "Enter longitude value", Toast.LENGTH_SHORT).show()
            }
            else if(!longitude.matches(Regex("^[0-9.+-]+$"))) {
                Toast.makeText(this@HotelOwnerReg, "Invalid longitude coordinate", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(latitude)){
                Toast.makeText(this@HotelOwnerReg, "Enter latitude value", Toast.LENGTH_SHORT).show()
            }
            else if(!latitude.matches(Regex("^[0-9.+-]+$"))) {
                Toast.makeText(this@HotelOwnerReg, "Invalid latitude coordinate", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(password)){
                Toast.makeText(this@HotelOwnerReg, "Enter Hotel Address", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(rePassword)){
                Toast.makeText(this@HotelOwnerReg, "ReEnter Password", Toast.LENGTH_SHORT).show()
            }

            if (password == rePassword){
                //If passwords are matching
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Register Successfully.",
                                Toast.LENGTH_SHORT,
                            ).show()

                            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                            val type = "Hotel Owner"

                            val userModel = UserModel(
                                userId,
                                type
                            )

                            var hotelOwnerModel = HotelOwnerModel(
                                name,
                                age,
                                hotelName,
                                longitude,
                                latitude,
                                hotelAddress
                            )

                            dbRef.child(userId).setValue(userModel)
                                .addOnCompleteListener {
                                    nameET.setText("")
                                }.addOnFailureListener() { err ->
                                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            dbRef2.child(userId).setValue(hotelOwnerModel)
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        this,
                                        "Register Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    ageET.setText("")
                                    hotelNameET.setText("")
                                    hotelAddressET.setText("")
                                }.addOnFailureListener() { err ->
                                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            //Redirecting the user after sign in is successful
                            val intent = Intent(this@HotelOwnerReg, HotelHome::class.java)
                            startActivity(intent)
                        } else {
                            //Display failed message if authentication fails
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                        nameET.text.clear()
                        ageET.text.clear()
                        hotelNameET.text.clear()
                        hotelAddressET.text.clear()
                        emailET.text.clear()
                        passwordET.text.clear()
                        confirmPasswordET.text.clear()
                    }
            } else {
                //If passwords does not match
                Toast.makeText(
                    baseContext,
                    "Password do not match.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}