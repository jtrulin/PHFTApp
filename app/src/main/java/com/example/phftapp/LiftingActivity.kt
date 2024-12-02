package com.example.phftapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class LiftingActivity : AppCompatActivity() {
    private var timeWhenStopped: Long = 0  // Track elapsed time when paused
    private var isRunning = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lifting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Buttons
        val chrono = findViewById<Chronometer>(R.id.chronometer)
        val startButton = findViewById<Button>(R.id.Start)
        val stopButton = findViewById<Button>(R.id.Stop)
        val progressButton = findViewById<Button>(R.id.chartProgress)

        // Spinner
        val spinnerID = findViewById<Spinner>(R.id.TypeOfLift)

        // Text
        val restTimeInput = findViewById<EditText>(R.id.TimeOfRest)
        val weightInPounds = findViewById<EditText>(R.id.weight)

        val lifts = arrayOf("Bench Press", "Squats", "Overhead press", "Deadlift")
        val arrayAdapt = ArrayAdapter(this@LiftingActivity, android.R.layout.simple_spinner_dropdown_item,lifts)
        spinnerID.adapter = arrayAdapt

        startButton.setOnClickListener {
            if (!isRunning) {
                // Start or resume the chronometer
                chrono.base = SystemClock.elapsedRealtime() + timeWhenStopped
                chrono.start()
                isRunning = true
                stopButton.text = "Stop"  // Change stop button to "Reset"
            }
        }

        // To be able to set the stopwatch, updates in real time
        restTimeInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Whenever the edit text changes, parse the input and set the chronometer base
                val restTimeSeconds = s.toString().toLongOrNull()
                if (restTimeSeconds != null && !isRunning) {
                    chrono.base = SystemClock.elapsedRealtime() - (restTimeSeconds * 1000)
                    timeWhenStopped = restTimeSeconds * 1000  // Update timeWhenStopped for consistency
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        stopButton.setOnClickListener{
            if(isRunning && stopButton.text == "Stop") {
                timeWhenStopped = SystemClock.elapsedRealtime() - chrono.base
                chrono.stop()
                isRunning = false
                stopButton.text = "Reset"  // Change stop button to "Reset"
                startButton.text = "Resume"  // Update start button to "Resume"
            }
            else {
                // Reset the chronometer
                chrono.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0  // resets time
                stopButton.text = "Stop"  // reverts stop button to "Stop"
                startButton.text = "Start"  // reverts start button to "Start"
            }

        }

        progressButton.setOnClickListener{
            val weightValue = weightInPounds.text.toString().toFloatOrNull() ?: 0.0f

            val intent = Intent(this, TrackWeightlifting::class.java)
            intent.putExtra("weight", weightValue)
            Toast.makeText(this, "in the chart", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }


        // Custom ArrayAdapter for the Spinner
        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, lifts) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).apply {
                    setTextColor(resources.getColor(R.color.white, null))
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
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
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) { // Ignore the default option
                    val selectedLift =  lifts[position]
                    Toast.makeText(this@LiftingActivity, "Selected: $selectedLift", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }



    }
}