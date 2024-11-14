package com.example.phftapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChooseActivity : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val runButton = findViewById<Button>(R.id.runningButton)

        runButton.setOnClickListener{
            val intent = Intent(this, RunningActivity::class.java)
            val actCon = ActivityContents(activityType = "Running")
            Toast.makeText(
                this,
                "Run Button Pressed: Activity Type = ${actCon.activityType}, Timer = ${actCon.timer}, Calories Burned = ${actCon.caloriesBurned}, Heart Rate = ${actCon.heartRate}",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }




    }
}