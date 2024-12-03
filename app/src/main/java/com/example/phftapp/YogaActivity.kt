package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class YogaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_yoga)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val isGuest = intent.getBooleanExtra("isGuest", false)
        val userId = intent.getIntExtra("userId", -1)

        // Buttons
        val chrono = findViewById<Chronometer>(R.id.chronometer)
        val startButton = findViewById<Button>(R.id.Start)
        val stopButton = findViewById<Button>(R.id.Stop)
        val doneButton = findViewById<Button>(R.id.Done)
        val progressButton = findViewById<Button>(R.id.chartProgress)
        val menuButton = findViewById<Button>(R.id.menu)


        // Text
        //val stressBeforeInput = findViewById<EditText>(R.id.stressBefore)
        //val stressAfterInput = findViewById<EditText>(R.id.stressAfter)
        val caloriesText = findViewById<TextView>(R.id.Calories)

        // Initialize Spinner
        val yogaPoseSpinner: Spinner = findViewById(R.id.yogaPoseSpinner)

        // List of Yoga Poses
        val yogaPoses = listOf(
            "Select a Pose",
            "Mountain Pose",
            "Chair Pose",
            "Tree Pose",
            "Bridge Pose",
            "Cat Pose",
            "Shavasana",
            "Downward Dog",
            "Cobra Pose",
            "High Lunge",
            "Triangle Pose"
        )

        // Custom ArrayAdapter for the Spinner
        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, yogaPoses) {
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

        // Attach the custom adapter to the Spinner
        yogaPoseSpinner.adapter = adapter

        // Handle Spinner item selection
        yogaPoseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) { // Ignore the default "Select a Pose" option
                    val selectedPose = yogaPoses[position]
                    Toast.makeText(this@YogaActivity, "Selected: $selectedPose", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
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
            finish()
        }
    }
}