package com.example.intelliguide.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.intelliguide.MainActivity
import com.example.intelliguide.R
import com.example.intelliguide.models.TouristModel
import com.example.intelliguide.models.UserModel
import com.example.intelliguide.tourist.tourist_home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class touristReg : AppCompatActivity() {

    private lateinit var nameET: EditText
    private lateinit var ageET: EditText
    private lateinit var countryET: EditText
    private lateinit var passNumberET: EditText
    private lateinit var contactET: EditText

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
        setContentView(R.layout.activity_tourist_reg)

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().getReference("userModel")
        dbRef2 = FirebaseDatabase.getInstance().getReference("touristModel")
        nameET = findViewById(R.id.TourRegName)
        ageET =  findViewById(R.id.TourRegAge)
        countryET =  findViewById(R.id.TourRegCountry)
        passNumberET =  findViewById(R.id.TourRegPass)
        contactET =  findViewById(R.id.TourRegConact)
        emailET =  findViewById(R.id.TourRegEmail)
        passwordET =  findViewById(R.id.TourRegPassword)
        confirmPasswordET =  findViewById(R.id.TourRegConPaas)
        register = findViewById(R.id.btnSignUp)
        reSignin = findViewById(R.id.btnSignIn)


        reSignin.setOnClickListener {
            val intent = Intent(this@touristReg, login::class.java)
            startActivity(intent)

        }

        register.setOnClickListener {

            var name: String = ""
            var age: String = ""
            var country: String = ""
            var passNumber: String = ""
            var contact: String = ""
            var email: String = ""
            var password: String = ""
            var rePassword: String = ""

            name = nameET.text.toString()
            email = emailET.text.toString()
            age = ageET.text.toString()
            country = countryET.text.toString()
            passNumber = passNumberET.text.toString()
            contact = contactET.text.toString()
            password = passwordET.text.toString()
            rePassword = confirmPasswordET.text.toString()

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this@touristReg, "Enter Email", Toast.LENGTH_SHORT).show()
                //return

            }

            if(TextUtils.isEmpty(name)){
                Toast.makeText(this@touristReg, "Enter name", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(age)){
                Toast.makeText(this@touristReg, "Enter Age", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(country)){
                Toast.makeText(this@touristReg, "Enter Country", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(contact)){
                Toast.makeText(this@touristReg, "Enter Email", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(passNumber)){
                Toast.makeText(this@touristReg, "Enter Passport Number", Toast.LENGTH_SHORT).show()
                //return

            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this@touristReg, "Enter a Password", Toast.LENGTH_SHORT).show()

            }
            if(TextUtils.isEmpty(rePassword)){
                Toast.makeText(this@touristReg, "ReEnter Password", Toast.LENGTH_SHORT).show()

            }

            if (password == rePassword) {
                // passwords match
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                            val type = "Tourist"


                            val userModel = UserModel(
                                userId,
                                type,
                                name,

                            )

                            var touristModel = TouristModel(
                                age,
                                country,
                                contact,
                                passNumber
                            )

                            dbRef.child(userId).setValue(userModel)
                                .addOnCompleteListener{
                                    nameET.setText("")
                                }.addOnFailureListener(){ err ->
                                    Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
                                }
                            dbRef2.child(userId).setValue(touristModel)
                                .addOnCompleteListener{
                                    Toast.makeText(this,"Register Successfully",Toast.LENGTH_SHORT).show()
                                    ageET.setText("")
                                    contactET.setText("")
                                    countryET.setText("")
                                    passNumberET.setText("")
                                }.addOnFailureListener(){ err ->
                                    Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
                                }




                            val intent = Intent(this@touristReg, tourist_home::class.java)
                            startActivity(intent)



                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
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