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

class hiit : AppCompatActivity() {
    private var timeWhenStopped: Long = 0  // Track elapsed time when paused
    private var ishiit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hiit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.hiit)) { v, insets ->
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
        val isGuest = intent.getBooleanExtra("isGuest", false)

        val chrono = findViewById<Chronometer>(R.id.chronometer)
        val startButton = findViewById<Button>(R.id.Start)
        val stopButton = findViewById<Button>(R.id.Stop)
        val doneButton = findViewById<Button>(R.id.Done)
        val menuButton = findViewById<Button>(R.id.menu)

        // Results
        val displayResult = findViewById<TextView>(R.id.TotalTimeDisplay)
        val displayDistance = findViewById<TextView>(R.id.TotalDistanceDisplayed)
        val displayPace = findViewById<TextView>(R.id.TotalPaceDisplayed)
        val displayCalories = findViewById<TextView>(R.id.TotalCaloriesDisplayed)
        val progressButton = findViewById<Button>(R.id.chartProgress)

        var caloriesBurned = 0.0f

        val user = User()  // User instance, replace with actual user data later with registration
        val activityContents = ActivityContents(activityType = "hiit")

        // GifImageView and its Drawable
        val hiitGif = findViewById<GifImageView>(R.id.hiitgif)
        val gifDrawable = hiitGif.drawable as GifDrawable

        gifDrawable.pause()

        startButton.setOnClickListener {
            if (!ishiit) {
                // Start or resume the chronometer
                chrono.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chrono.start()
                ishiit = true

                gifDrawable.start()

                stopButton.text = "Stop"  // Change stop button to "Reset"
                displayResult.text = ""
                displayCalories.text = ""
            }
        }

        stopButton.setOnClickListener {
            if (ishiit && stopButton.text == "Stop") {
                // Pause the chronometer
                timeWhenStopped = SystemClock.elapsedRealtime() - chrono.base
                chrono.stop()
                ishiit = false

                gifDrawable.pause()

                stopButton.text = "Reset"  // Change stop button to "Reset"
                startButton.text = "Resume"  // Update start button to "Resume"
                displayResult.text = ""
                displayCalories.text = ""
            } else {
                // Reset the chronometer
                chrono.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0  // Resets time
                stopButton.text = "Stop"  // Revert stop button to "Stop"
                startButton.text = "Start"  // Revert start button to "Start"

                // Reset the GIF and pause it
                gifDrawable.stop()
                gifDrawable.seekToFrame(0) // Reset to the first frame
                gifDrawable.pause()

                displayResult.text = ""
                displayCalories.text = ""
            }
        }

        doneButton.setOnClickListener {
            val elapsedTimeInMilliseconds = timeWhenStopped
            // Milliseconds to seconds, but simulating it as minutes for users
            val elapsedTimeInMinutes = elapsedTimeInMilliseconds / 1000
            Toast.makeText(this, "Total Time: $elapsedTimeInMinutes", Toast.LENGTH_SHORT).show()


            // Passing User info to get and save calories, timer in ActivityContents is updated
            activityContents.timer = elapsedTimeInMinutes.toFloat()
            caloriesBurned = activityContents.trackCalories(user) // Uses the specific MET to calculate

            displayResult.text = "Total Time: $elapsedTimeInMinutes minutes"
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

        progressButton.setOnClickListener {
            val intent = Intent(this, Tracking::class.java)
            intent.putExtra("calories", caloriesBurned)
            Toast.makeText(
                this,
                "in the chart",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }

        menuButton.setOnClickListener {
            val intent = Intent(this, ChooseActivity::class.java)
            if(isGuest){
                intent.putExtra("isGuest", true)
            } else {
                intent.putExtra("userId", userId) // Pass userId with correct key casing
            }
            startActivity(intent)
        }
    }
}