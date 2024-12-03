package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenu : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = DatabaseHelper(this)


        // Retrieve data from intent
        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val isGuest = intent.getBooleanExtra("isGuest", false)
        val userId = intent.getIntExtra("userId", -1) // Default to -1 if no userId is provided

        // Reference the TextView for user info
        val userInfoTextView = findViewById<TextView>(R.id.tvUserInfo)


        // Buttons
        val activityButton = findViewById<ImageButton>(R.id.wrkButton)
        val socialButton = findViewById<ImageButton>(R.id.socButton)
        val trainerButton = findViewById<ImageButton>(R.id.reqButton)
        val logoutButton = findViewById<Button>(R.id.loutButton)
        val goalsButton = findViewById<Button>(R.id.goalButton)

        // knows if user is logged in or if it's a guest, use later to limit functionalities
        //val isGuest = intent.getBooleanExtra("isGuest", false) // retrieves flag, changes to true if a user is a guest
        // (passed in LoginPage.kt for logged in user and MainMenu.kt for guest



        // Handle guest users
        if (isGuest) {
            userInfoTextView.text = "Welcome, Guest"
            Toast.makeText(this, "Welcome, Guest! Limited features available.", Toast.LENGTH_SHORT).show()
        } else {
            if (userId == -1) {
                Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
                finish() // Exit if userId is invalid
            } else {
                val userName = databaseHelper.getUserNameById(userId)
                if (userName != null) {
                    userInfoTextView.text = "Welcome, $userName\nID: $userId"
                } else {
                    userInfoTextView.text = "Welcome, User"
                    Toast.makeText(this, "Failed to fetch user information.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        activityButton.setOnClickListener(){
            val intent = Intent(this, ChooseActivity::class.java)
            if (isGuest) {
                intent.putExtra("isGuest", true)
            } else {
                intent.putExtra("userId", userId) // Pass userId with correct key casing
            }
            startActivity(intent)
        }


        goalsButton.setOnClickListener(){
            val intent = Intent(this, GoalsPage::class.java)
            if (isGuest) {
                intent.putExtra("isGuest", true)
            } else {
                intent.putExtra("userId", userId) // Pass userId
                intent.putExtra("email", email) // Pass email
                intent.putExtra("password", password) // Pass password
            }
            startActivity(intent)
        }

        socialButton.setOnClickListener(){
            if(isGuest){
                Toast.makeText(this, "Guests don't have access! Please Register", Toast.LENGTH_SHORT).show()
            }
        }

        trainerButton.setOnClickListener(){
            if(isGuest){
                Toast.makeText(this, "Guests don't have access! Please Register", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, Trainer::class.java)
                startActivity(intent)
            }
        }

        val paymentValidationButton = findViewById<ImageButton>(R.id.paymentValidationButton)

        paymentValidationButton.setOnClickListener(){
            if(isGuest){
                Toast.makeText(this, "Please Register First!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RegisterPage::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this, PaymentValidation::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }

        }

        logoutButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val socialmediabutton = findViewById<ImageButton>(R.id.socButton)

        socialmediabutton.setOnClickListener(){
            val intent = Intent(this, socialmedia::class.java)
            startActivity(intent)
        }
    }
}