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
    private var caloriesBurned = 0.0f

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
        val menuButton = findViewById<Button>(R.id.menu)
        val progressButton = findViewById<Button>(R.id.chartProgress)

        // Results
        val displayResult = findViewById<TextView>(R.id.TotalTimeDisplay)
        val displayDistance = findViewById<TextView>(R.id.TotalDistanceDisplayed)
        val displayPace = findViewById<TextView>(R.id.TotalPaceDisplayed)
        val displayCalories = findViewById<TextView>(R.id.TotalCaloriesDisplayed)

        // User and ActivityContents setup
        val user = User() // Replace with actual user data
        val activityContents = ActivityContents(activityType = "running")

        // GifImageView and its Drawable
        val runningGif = findViewById<GifImageView>(R.id.runningGif)
        val gifDrawable = runningGif.drawable as GifDrawable

        gifDrawable.pause() // Start in paused state

        startButton.setOnClickListener {
            if (!isRunning) {
                // Start or resume the chronometer
                chrono.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chrono.start()
                isRunning = true

                gifDrawable.start() // Start GIF animation

                stopButton.text = "Stop"
                displayResult.text = ""
                displayDistance.text = ""
                displayPace.text = ""
                displayCalories.text = ""
            }
        }

        stopButton.setOnClickListener {
            if (isRunning && stopButton.text == "Stop") {
                timeWhenStopped = SystemClock.elapsedRealtime() - chrono.base
                chrono.stop()
                isRunning = false

                gifDrawable.pause() // Pause GIF animation

                stopButton.text = "Reset"
                startButton.text = "Resume"
                displayResult.text = ""
                displayDistance.text = ""
                displayPace.text = ""
                displayCalories.text = ""
            } else {
                // Reset the chronometer
                chrono.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0
                stopButton.text = "Stop"
                startButton.text = "Start"

                gifDrawable.stop() // Reset GIF animation
                gifDrawable.seekToFrame(0) // Return to the first frame
                gifDrawable.pause() // Keep it paused

                displayResult.text = ""
                displayDistance.text = ""
                displayPace.text = ""
                displayCalories.text = ""
            }
        }

        doneButton.setOnClickListener {
            val elapsedTimeInMilliseconds = timeWhenStopped
            val elapsedTimeInMinutes = elapsedTimeInMilliseconds / 1000 / 60
            Toast.makeText(this, "Total Time: $elapsedTimeInMinutes minutes", Toast.LENGTH_SHORT).show()

            val totalDistance: Double = (elapsedTimeInMinutes * 160.0) / 1600.0
            val totalPace: Double = if (totalDistance > 0) elapsedTimeInMinutes / totalDistance else 0.0

            activityContents.timer = elapsedTimeInMinutes.toFloat()
            caloriesBurned = activityContents.trackCalories(user)

            displayResult.text = "Total Time: $elapsedTimeInMinutes minutes"
            displayDistance.text = "Total Distance: $totalDistance miles"
            displayPace.text = "Total Pace: $totalPace minutes per mile"
            displayCalories.text = "Calories Burned: $caloriesBurned"
        }

        progressButton.setOnClickListener {
            val intent = Intent(this, Tracking::class.java)
            intent.putExtra("calories", caloriesBurned)
            Toast.makeText(this, "In the chart", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        menuButton.setOnClickListener {
            val intent = Intent(this, ChooseActivity::class.java)
            startActivity(intent)
        }
    }
}
