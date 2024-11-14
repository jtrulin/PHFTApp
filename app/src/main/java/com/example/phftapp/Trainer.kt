package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Trainer : AppCompatActivity() {

    private var totalRatings = 4.5f  // Example initial average rating
    private var ratingCount = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trainer)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TrainerID)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar1)
        val overallRatingText = findViewById<TextView>(R.id.overall_rating)

        // Display initial overall rating
        updateOverallRatingText(overallRatingText)

        // Set up listener for user rating interaction
        ratingBar.setOnRatingBarChangeListener { _, userRating, _ ->
            // Update the total ratings and count (simulate adding a new rating)
            totalRatings += userRating
            ratingCount += 1

            // Calculate new overall rating
            updateOverallRatingText(overallRatingText)

         val messages = findViewById<Button>(R.id.message_button)
            messages.setOnClickListener(){
                val intent = Intent(this, Messagetrainer::class.java)
                startActivity(intent)
            }

        }
    }

    // Helper function to update the overall rating TextView
    private fun updateOverallRatingText(overallRatingText: TextView) {
        val averageRating = totalRatings / ratingCount
        overallRatingText.text = "Overall Rating: ${String.format("%.1f", averageRating)}"

    val messagebutton = findViewById<Button>(R.id.message_button)

        messagebutton.setOnClickListener(){
            val intent = Intent(this, Messagetrainer::class.java)
            startActivity(intent)
        }

    }
}
