package com.example.seniorproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.seniorproject.model.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register);

        register_button.setOnClickListener {
        registerUser()

        }
        already_have_account_textview.setOnClickListener {
            redirectToLogin()
        }

    }

    private fun registerUser(){
        val email = email_signup_editText.text.toString()
        val password = password_signup_editTExt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("Debug", "Email: " + email)
        Log.d("Debug", "pass: " + password)

        //Firebase Authentication
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                // else if successful
                Log.d("Debug","NEW USER, uid: ${it.result?.user?.uid}")
                Toast.makeText(this, "Account Creation successful", Toast.LENGTH_SHORT).show()
                saveUserToFirebaseDatabase()
                redirectToLogin()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun redirectToLogin(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    private fun saveUserToFirebaseDatabase(){
        Log.d("Debug", "entered firebase database function")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("university-social-media/$uid")
        val user = User(username_signup_editext.text.toString(),
            email_signup_editText.text.toString(),password_signup_editTExt.text.toString())

        ref.setValue(user).addOnCompleteListener(this){
                    task ->  if (task.isSuccessful){
                Log.d("Debug", "saving to database worked")
            } else {
                Log.d("Debug", "not saved")
            }}.addOnFailureListener(){
                Log.d("Debug", "Error ${it.message}")
            }
    }

}
