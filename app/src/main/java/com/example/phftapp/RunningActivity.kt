package com.example.phftapp

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

class RunningActivity : AppCompatActivity() {

    private var timeWhenStopped: Long = 0  // Track elapsed time when paused
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_running)
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

        // Results
        var displayResult = findViewById<TextView>(R.id.TotalTimeDisplay)
        var displayDistance = findViewById<TextView>(R.id.TotalDistanceDisplayed)
        var displayPace = findViewById<TextView>(R.id.TotalPaceDisplayed)
        var displayCalories = findViewById<TextView>(R.id.TotalCaloriesDisplayed)

        val user = User()  // User instance, replace with actual user data later with registration
        val activityContents = ActivityContents(activityType = "running")

        startButton.setOnClickListener {
            if (!isRunning) {
                // Start or resume the chronometer
                chrono.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chrono.start()
                isRunning = true
                stopButton.text = "Stop"  // Change stop button to "Reset"
                displayResult.text= ""
                displayDistance.text = ""
                displayPace.text = ""


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
                displayPace.text = ""

            }
            else {
                // Reset the chronometer
                chrono.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0  // resets time
                stopButton.text = "Stop"  // reverts stop button to "Stop"
                startButton.text = "Start"  // reverts start button to "Start"
                displayResult.text= ""
                displayDistance.text = ""
                displayPace.text = ""
            }

        }

        doneButton.setOnClickListener {
            val elapsedTimeInMilliseconds = timeWhenStopped
            // milliseconds to seconds, but simulating it as minutes for users
            val elapsedTimeInMinutes= elapsedTimeInMilliseconds / 1000
            Toast.makeText(this, "Total Time: $elapsedTimeInMinutes", Toast.LENGTH_SHORT).show()

            // every minute, 160 meters are ran, divided by 16000 for 1 mile
            // for example, do 25 seconds for 2 miles (on average)
            val totalDistance: Double = (elapsedTimeInMinutes * 160.0) / 1600.0

            // formula for pace
            val totalPace: Double = elapsedTimeInMinutes/totalDistance

            // passing User info to get and save calories, timer in ActivityContents is updated
            val caloriesBurned = activityContents.trackCalories(user)
            activityContents.timer = elapsedTimeInMinutes.toFloat()



            displayResult.text = "Total Time: $elapsedTimeInMinutes minutes"
            displayDistance.text = "Total Distance: $totalDistance miles"
            displayPace.text = "Total Pace: $totalPace minutes per mile"
            displayCalories.text = "Calories Burned: $caloriesBurned"


        }
    }
}