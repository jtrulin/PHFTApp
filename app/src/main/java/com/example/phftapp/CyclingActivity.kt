package com.example.phftapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CyclingActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cycling)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve userId passed from MainMenu
        val userId = intent.getIntExtra("userId", -1)
        /*
        if (userId == -1) {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
            finish() // Exit if no userId is found
        }*/

        // Buttons and Texts
        val distanceInput = findViewById<EditText>(R.id.distanceInput)
        val durationInput = findViewById<EditText>(R.id.durationInput)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val progressButton = findViewById<Button>(R.id.chartProgress)
        val menuButton = findViewById<Button>(R.id.menu)
        val caloriesOutput = findViewById<TextView>(R.id.caloriesOutput)
        val speedOutput = findViewById<TextView>(R.id.speedOutput)

        val user = User()  // User instance, replace with actual user data later with registration
        val activityContents = ActivityContents(activityType = "cycling")
        var caloriesBurned = 0.0f

        calculateButton.setOnClickListener{
            val distance = distanceInput.text.toString().toFloat()
            val duration = durationInput.text.toString().toFloat()

            val speed = distance / (duration / 60f) // Minutes to Hours

            // Passing User info to get and save calories, timer in ActivityContents is updated to duration of Cycling
            activityContents.timer = duration
            caloriesBurned = activityContents.trackCalories(user) // Uses Cycling met to calculate calories

            // Display results
            speedOutput.text = "Speed: ${"%.2f".format(speed)} mph"
            caloriesOutput.text = "Calories Burned: ${"%.2f".format(caloriesBurned)}"

            // Add calories burned to the progressReport database
            val databaseHelper = DatabaseHelper(this)
            //val userId = intent.getIntExtra("userId", -1) // Ensure userId is passed to this activity
            if (userId != -1) {
                val progressReport = ProgressReport(caloriesBurned = caloriesBurned.toDouble(), userId = userId)
                val insertedId = databaseHelper.insertProgress(progressReport)
                if (insertedId != -1L) {
                    Toast.makeText(this, "Progress saved successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to save progress.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not logged in. Progress not saved.", Toast.LENGTH_SHORT).show()
            }
        }

        progressButton.setOnClickListener{
            val intent = Intent(this, Tracking::class.java)
            intent.putExtra("calories", caloriesBurned)
            Toast.makeText(this, "in the chart", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        menuButton.setOnClickListener{
            val intent = Intent(this, ChooseActivity::class.java)
            startActivity(intent)
        }

    }
}