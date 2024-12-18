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
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

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

        // Retrieve userId passed from MainMenu
        val userId = intent.getIntExtra("userId", -1)
        val isGuest = intent.getBooleanExtra("isGuest", false)

        // Buttons
        val chrono = findViewById<Chronometer>(R.id.chronometer)
        val startButton = findViewById<Button>(R.id.Start)
        val stopButton = findViewById<Button>(R.id.Stop)
        val doneButton = findViewById<Button>(R.id.Done)
        val menuButton = findViewById<Button>(R.id.menu)

        val displayResult = findViewById<TextView>(R.id.TotalTimeDisplay)
        val displayDistance = findViewById<TextView>(R.id.TotalDistanceDisplayed)
        val displayPace = findViewById<TextView>(R.id.TotalPaceDisplayed)
        val displayCalories = findViewById<TextView>(R.id.TotalCaloriesDisplayed)
        val progressButton = findViewById<Button>(R.id.chartProgress)

        // GifImageView and its Drawable
        val runningGif = findViewById<GifImageView>(R.id.runningGif)
        val gifDrawable = runningGif.drawable as GifDrawable
        gifDrawable.pause()

        var caloriesBurned = 0.0f
        val user = User()  // Replace with actual user data later
        val activityContents = ActivityContents(activityType = "running")

        startButton.setOnClickListener {
            if (!isRunning) {
                // Start or resume the chronometer
                chrono.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chrono.start()
                isRunning = true
                gifDrawable.start()
                stopButton.text = "Stop"  // Change stop button to "Reset"
                displayResult.text = ""
                displayDistance.text = ""
                displayPace.text = ""
                displayCalories.text = ""
            }
        }

        stopButton.setOnClickListener {
            if (isRunning && stopButton.text == "Stop") {
                // Pause the chronometer
                timeWhenStopped = SystemClock.elapsedRealtime() - chrono.base
                chrono.stop()
                isRunning = false
                gifDrawable.pause()
                stopButton.text = "Reset"  // Change stop button to "Reset"
                startButton.text = "Resume"  // Update start button to "Resume"
            } else {
                // Reset the chronometer
                chrono.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0
                gifDrawable.stop()
                gifDrawable.seekToFrame(0) // Reset to the first frame
                gifDrawable.pause()
                stopButton.text = "Stop"  // Revert stop button to "Stop"
                startButton.text = "Start"  // Revert start button to "Start"
                displayResult.text = ""
                displayDistance.text = ""
                displayPace.text = ""
                displayCalories.text = ""
            }

        }

        doneButton.setOnClickListener {
            val elapsedTimeInMilliseconds = timeWhenStopped
            val elapsedTimeInMinutes = elapsedTimeInMilliseconds / 1000
            Toast.makeText(this, "Total Time: $elapsedTimeInMinutes", Toast.LENGTH_SHORT).show()

            // Calculate distance, pace, and calories
            val totalDistance: Double = (elapsedTimeInMinutes * 160.0) / 1600.0
            val totalPace: Double = elapsedTimeInMinutes / totalDistance
            activityContents.timer = elapsedTimeInMinutes.toFloat()
            caloriesBurned = activityContents.trackCalories(user)

            displayResult.text = "Total Time: $elapsedTimeInMinutes minutes"
            displayDistance.text = "Total Distance: $totalDistance miles"
            displayPace.text = "Total Pace: $totalPace minutes per mile"
            displayCalories.text = "Calories Burned: $caloriesBurned"


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
            Toast.makeText(
                this,
                "in the chart",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }

        menuButton.setOnClickListener{
            val intent = Intent(this, ChooseActivity::class.java)
            //val userId = intent.getIntExtra("userId", -1) // Retrieve the userId passed to RunningActivity
            if (isGuest) {
                intent.putExtra("isGuest", true)
            } else {
                intent.putExtra("userId", userId) // Pass userId back to ChooseActivity
            }
            startActivity(intent)
            finish() // Close RunningActivity
        }
    }
}