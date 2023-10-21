package com.example.intelliguide.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.intelliguide.R
import com.example.intelliguide.models.PoliceModel
import com.example.intelliguide.models.UserModel
import com.example.intelliguide.tourist.tourist_home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class TouristPoliceReg : AppCompatActivity() {

    private lateinit var nameET: EditText
    private lateinit var policeIdET: EditText
    private lateinit var allocatedStationET: EditText

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
        setContentView(R.layout.activity_tourist_police_reg)

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().getReference("userModel")
        dbRef2 = FirebaseDatabase.getInstance().getReference("policeModel")

        nameET = findViewById(R.id.touristPoliceRegEditText1)
        policeIdET =  findViewById(R.id.touristPoliceRegEditText2)
        allocatedStationET = findViewById(R.id.touristPoliceRegEditText3)
        emailET =  findViewById(R.id.touristPoliceRegEditText6)
        passwordET =  findViewById(R.id.touristPoliceRegEditText7)
        confirmPasswordET =  findViewById(R.id.touristPoliceRegEditText8)
        register = findViewById(R.id.touristPoliceRegButton1)
        reSignin = findViewById(R.id.touristPoliceRegButton2)

        reSignin.setOnClickListener {
            val intent = Intent(this@TouristPoliceReg, login::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {

            var name: String = ""
            var policeId: String = ""
            var allocatedStation: String = ""
            var email: String = ""
            var password: String = ""
            var rePassword: String = ""

            name = nameET.text.toString()
            email = emailET.text.toString()
            policeId = policeIdET.text.toString()
            allocatedStation = allocatedStationET.text.toString()
            password = passwordET.text.toString()
            rePassword = confirmPasswordET.text.toString()

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this@TouristPoliceReg, "Enter Email", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this@TouristPoliceReg, "Enter name", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(policeId)){
                Toast.makeText(this@TouristPoliceReg, "Enter Police ID", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(allocatedStation)){
                Toast.makeText(this@TouristPoliceReg, "Enter Allocated Station", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this@TouristPoliceReg, "Enter a Password", Toast.LENGTH_SHORT).show()

            }
            if(TextUtils.isEmpty(rePassword)){
                Toast.makeText(this@TouristPoliceReg, "ReEnter Password", Toast.LENGTH_SHORT).show()

            }

            if (password == rePassword){
                //If passwords match
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Register Sucessfully.",
                                Toast.LENGTH_SHORT,
                            ).show()

                            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                            val type = "Tourist Police"

                            val userModel = UserModel(
                                userId,
                                type,
                                name,
                            )

                            var policeModel = PoliceModel(
                                policeId,
                                allocatedStation
                            )

                            dbRef.child(userId).setValue(userModel)
                                .addOnCompleteListener {
                                    nameET.setText("")
                                }.addOnFailureListener() { err ->
                                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            dbRef2.child(userId).setValue(policeModel)
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        this,
                                        "Register Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    policeIdET.setText("")
                                    allocatedStationET.setText("")
                                }.addOnFailureListener() { err ->
                                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            //Add the page where you want to redirect after signing up
                            val intent = Intent(this@TouristPoliceReg, tourist_home::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                        nameET.text.clear()
                        policeIdET.text.clear()
                        allocatedStationET.text.clear()

                        emailET.text.clear()
                        passwordET.text.clear()
                        confirmPasswordET.text.clear()
                    }
                    } else {
                        // passwords don't match
                        Toast.makeText(
                            baseContext,
                            "Password do not match.",
                            Toast.LENGTH_SHORT,
                        ).show()
            }
        }

    }
}