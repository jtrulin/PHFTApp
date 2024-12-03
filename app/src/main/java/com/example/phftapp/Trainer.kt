package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Trainer : AppCompatActivity() {

    private var totalRatings = 0.0f  // Example initial average rating
    private var ratingCount = 10
    private val sharedPref by lazy { getSharedPreferences("TrainerRatings", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trainer)

        // Load ratings from SharedPreferences
        totalRatings = sharedPref.getFloat("totalRatings", 4.5f)
        ratingCount = sharedPref.getInt("ratingCount", 10)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TrainerID)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar1)
        val overallRatingText = findViewById<TextView>(R.id.overall_rating)
        val messages = findViewById<Button>(R.id.message_button)
        val backToMenuButton = findViewById<Button>(R.id.backToMenuButton)

        // Display initial overall rating
        updateOverallRatingText(overallRatingText)

        // Handle rating changes
        ratingBar.setOnRatingBarChangeListener { _, userRating, _ ->
            totalRatings += userRating
            ratingCount += 1
            saveRatings()
            updateOverallRatingText(overallRatingText)
        }

        messages.setOnClickListener {
            val intent = Intent(this, Messagetrainer::class.java)
            startActivity(intent)
        }

        backToMenuButton.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateOverallRatingText(overallRatingText: TextView) {
        val averageRating = totalRatings / ratingCount
        overallRatingText.text = "Overall Rating: ${String.format("%.1f", averageRating)}"
    }

    private fun saveRatings() {
        with(sharedPref.edit()) {
            putFloat("totalRatings", totalRatings)
            putInt("ratingCount", ratingCount)
            apply()
        }
    }
}
