package com.example.phftapp

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

class RunningActivity : AppCompatActivity() {

    private var timeWhenStopped: Long = 0  // Track elapsed time when paused
    private var isRunning = false

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_running)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve userId passed from MainMenu
        val userId = intent.getIntExtra("userId", -1)

        if (userId == -1) {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
            finish() // Exit if no userId is found
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
        var displayPace = findViewById<TextView>(R.id.TotalPaceDisplayed)
        var displayCalories = findViewById<TextView>(R.id.TotalCaloriesDisplayed)
        val progressButton = findViewById<Button>(R.id.chartProgress)

        var caloriesBurned = 0.0f

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
                displayPace.text = ""
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
                displayPace.text = ""
                displayCalories.text =""

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
            activityContents.timer = elapsedTimeInMinutes.toFloat()
            caloriesBurned = activityContents.trackCalories(user) //uses the specific MET to calculate

            displayResult.text = "Total Time: $elapsedTimeInMinutes minutes"
            displayDistance.text = "Total Distance: $totalDistance miles"
            displayPace.text = "Total Pace: $totalPace minutes per mile"
            displayCalories.text = "Calories Burned: $caloriesBurned"


            // Add calories burned to the progressReport database
            val databaseHelper = DatabaseHelper(this)
            val userId = intent.getIntExtra("userId", -1) // Ensure userId is passed to this activity
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