package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val submitButton = findViewById<Button>(R.id.sButton)
        val validEmail = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val validPassword = findViewById<EditText>(R.id.editTextTextPassword)

        submitButton.setOnClickListener {
            //if(validateCredentials(validEmail,validPassword)){
                // run when login is successful
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainMenu::class.java)
                val isGuest = intent.getBooleanExtra("isGuest", false) // sets a flag to show a user is a guest

            startActivity(intent)
           // }
        }
    }




    private fun validateCredentials(validEmail: EditText, validPassword: EditText): Boolean { // validEmail and validPassword are of type "EditText"
        val email = validEmail.text.toString()
        val password = validPassword.text.toString()
        val atPosition = email.indexOf('@') // helps finding the '.' AFTER '@'. if returned true then gives position, if not it returns -1

        if(email.isEmpty() || email.indexOf('@') == -1 || email.indexOf('.', atPosition) == -1) {

            // run when email is invalid
            Toast.makeText(this, "Cannot login! Invalid email", Toast.LENGTH_SHORT).show()
            return false
        }

        else if (password.isEmpty() || password.length < 6) {
            // run when password is invalid
            Toast.makeText(this, "Cannot login! Invalid password", Toast.LENGTH_SHORT).show()
            return false
        }
        else {
            return true // returns true only if conditions are met
        }
    }
}