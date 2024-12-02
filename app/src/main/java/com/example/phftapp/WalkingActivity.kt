package com.example.phftapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WalkingActivity : AppCompatActivity() {

    private var timeWhenStopped: Long = 0  // Track elapsed time when paused
    private var isRunning = false
    private var steps = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_walking)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Buttons
        val chrono = findViewById<Chronometer>(R.id.chronometer)
        val startButton = findViewById<Button>(R.id.Start)
        val stopButton = findViewById<Button>(R.id.Stop)
        val doneButton = findViewById<Button>(R.id.Done)
        val menuButton = findViewById<Button>(R.id.menu)

        // Results
        var displayResult = findViewById<TextView>(R.id.TotalTimeDisplay)
        var displayDistance = findViewById<TextView>(R.id.TotalDistanceDisplayed)
        var displayCalories = findViewById<TextView>(R.id.TotalCaloriesDisplayed)
        val progressButton = findViewById<Button>(R.id.chartProgress)

        var caloriesBurned = 0.0f

        val user = User()  // User instance, replace with actual user data later with registration
        val activityContents = ActivityContents(activityType = "walking")

        startButton.setOnClickListener {
            if (!isRunning) {
                // Start or resume the chronometer
                chrono.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chrono.start()
                isRunning = true
                stopButton.text = "Stop"  // Change stop button to "Reset"
                displayResult.text= ""
                displayDistance.text = ""
                displayCalories.text =""


            }
        }

        stopButton.setOnClickListener{
            if(isRunning && stopButton.text == "Stop") {
                timeWhenStopped = SystemClock.elapsedRealtime() - chrono.base
                chrono.stop()
                isRunning = false
                stopButton.text = "Reset"  // Change stop button to "Reset"
                startButton.text = "Resume"  // Update start button to "Resume"
                displayResult.text= ""
                displayDistance.text = ""
                displayCalories.text =""
            }
            else {
                // Reset the chronometer
                chrono.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0  // resets time
                stopButton.text = "Stop"  // reverts stop button to "Stop"
                startButton.text = "Start"  // reverts start button to "Start"
                displayResult.text= ""
                displayDistance.text = ""
                displayCalories.text =""

            }

        }

        doneButton.setOnClickListener {
            val elapsedTimeInMilliseconds = timeWhenStopped

            // milliseconds to seconds, but simulating it as minutes for users
            val elapsedTimeInMinutes= elapsedTimeInMilliseconds / 1000
            Toast.makeText(this, "Total Time: $elapsedTimeInMinutes", Toast.LENGTH_SHORT).show()

            val strideLength = user.height * 0.415f // Average stride length
            val speedMetersPerSecond = 1.3f // Average walking speed
            val totalDistance = elapsedTimeInMinutes * speedMetersPerSecond  // Distance in meters
            steps = (totalDistance / strideLength).toInt()


            // passing User info to get and save calories, timer in ActivityContents is updated
            activityContents.timer = elapsedTimeInMinutes.toFloat()
            caloriesBurned = activityContents.trackCalories(user)

            displayResult.text = "Total Time: $elapsedTimeInMinutes minutes"
            displayDistance.text = "Total Steps: $totalDistance"
            displayCalories.text = "Calories Burned: $caloriesBurned"
        }

        progressButton.setOnClickListener{
            val intent = Intent(this, Tracking::class.java)
            intent.putExtra("calories", caloriesBurned)
            Toast.makeText(
                this,
                "in the chart",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }

        menuButton.setOnClickListener{
            val intent = Intent(this, ChooseActivity::class.java)
            startActivity(intent)
        }


    }
}