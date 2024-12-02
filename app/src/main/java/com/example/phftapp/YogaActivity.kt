package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class YogaActivity : AppCompatActivity() {
    private var timeWhenStopped: Long = 0  // Track elapsed time when paused
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_yoga)
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
        val progressButton = findViewById<Button>(R.id.chartProgress)


        // Text
        //val stressBeforeInput = findViewById<EditText>(R.id.stressBefore)
        //val stressAfterInput = findViewById<EditText>(R.id.stressAfter)
        val caloriesText = findViewById<TextView>(R.id.Calories)

        // Spinner
        val spinnerID = findViewById<Spinner>(R.id.yogaPoseSpinner)

        // Types of Yoga Activities
        val yogaTypes = arrayOf("Yin Yoga", "Power Yoga", "Hatha Yoga", "Restorative Yoga")
        val arrayAdapt = ArrayAdapter(
            this@YogaActivity,
            android.R.layout.simple_spinner_dropdown_item,
            yogaTypes
        )
        spinnerID.adapter = arrayAdapt

        var caloriesBurned = 0.0f

        val user = User()  // User instance, replace with actual user data later with registration
        val activityContents = ActivityContents(activityType = "yoga")

        startButton.setOnClickListener {
            if (!isRunning) {
                // Start or resume the chronometer
                chrono.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chrono.start()
                isRunning = true

                stopButton.text = "Stop"  // Change stop button to "Reset"
            }
        }

        stopButton.setOnClickListener {
            if (isRunning && stopButton.text == "Stop") {
                // Pause the chronometer
                timeWhenStopped = SystemClock.elapsedRealtime() - chrono.base
                chrono.stop()
                isRunning = false

                stopButton.text = "Reset"  // Change stop button to "Reset"
                startButton.text = "Resume"  // Update start button to "Resume"
            } else {
                // Reset the chronometer
                chrono.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0  // Resets time
                stopButton.text = "Stop"  // Revert stop button to "Stop"
                startButton.text = "Start"  // Revert start button to "Start"
                caloriesText.text = "Calories Burned: "

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

            caloriesText.text = "Calories Burned: $caloriesBurned cal"

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

        // Custom ArrayAdapter for the Spinner
        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, yogaTypes) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).apply {
                    setTextColor(resources.getColor(R.color.white, null))
                }
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).apply {
                    setTextColor(resources.getColor(R.color.white, null))
                    setBackgroundColor(resources.getColor(R.color.darkGray, null))
                }
                return view
            }
        }

        // Apply the custom adapter
        spinnerID.adapter = adapter

        // Handle Spinner item selection
        spinnerID.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) { // Ignore the default option
                    val selectedYoga = yogaTypes[position]
                    Toast.makeText(
                        this@YogaActivity,
                        "Selected: $selectedYoga",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            // No action needed
            }
        }
    }
}