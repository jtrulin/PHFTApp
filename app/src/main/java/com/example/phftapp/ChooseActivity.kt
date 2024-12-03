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
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class ChooseActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = DatabaseHelper(this)

        //val userInfoTextView = findViewById<TextView>(R.id.tvUserInfo)

        val heartbeatIcon = findViewById<ImageView>(R.id.heartbeatIcon)
        val pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse)
        heartbeatIcon.startAnimation(pulseAnimation)

        val isGuest = intent.getBooleanExtra("isGuest", false)
        var activityType = "running" // default activity is running

        // Retrieve userId from Intent
        val userId = intent.getIntExtra("userId", -1)

        if (!isGuest && userId == -1) {
            finish() // Exit if userId is invalid
        }


        // initializing buttons
        val runButton = findViewById<Button>(R.id.runningButton)
        val walkButton = findViewById<Button>(R.id.walkingButton)
        val cycleButton = findViewById<Button>(R.id.cyclingButton)
        val yogaButton = findViewById<Button>(R.id.yogaButton)
        val liftButton = findViewById<Button>(R.id.weightliftButton)
        val Hiitbutton = findViewById<Button>(R.id.hiitButton)

        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            if(isGuest){
                intent.putExtra("isGuest", true)
            } else {
                intent.putExtra("userId", userId) // Pass userId with correct key casing
            }
            startActivity(intent)
            finish()
        }

        runButton.setOnClickListener {
            if (isGuest) {
                // If user a guest, show the ad page
                val intent = Intent(this, AdPage::class.java)
                intent.putExtra("activityType", activityType) // ad page goes to the intended activity after pressing continue
                intent.putExtra("isGuest", true)
                startActivity(intent)
            } else {
                val intent = Intent(this, RunningActivity::class.java)

                if (userId != -1) {
                    // else, proceed to RunningActivity
                    //val intent = Intent(this, RunningActivity::class.java)
                    intent.putExtra("userId", userId)
                    val actCon = ActivityContents(activityType = "running")
                    
                }
                startActivity(intent)
            }
        }

        walkButton.setOnClickListener{
            if (isGuest) {
                // If user a guest, show the ad page
                activityType = "walking"
                val intent = Intent(this, AdPage::class.java)
                intent.putExtra("activityType", activityType) // ad page goes to the intended activity after pressing continue
                intent.putExtra("isGuest", true)
                startActivity(intent)
            } else {
                // else, proceed to WalkingActivity
                val intent = Intent(this, WalkingActivity::class.java)
                val actCon = ActivityContents(activityType = "walking")

                startActivity(intent)
            }
        }

        cycleButton.setOnClickListener{
            if (isGuest) {
                // If user a guest, show the ad page
                activityType = "cycling"
                val intent = Intent(this, AdPage::class.java)
                intent.putExtra("activityType", activityType) // ad page goes to the intended activity after pressing continue
                intent.putExtra("isGuest", true)
                startActivity(intent)
            } else {
                // else, proceed to WalkingActivity
                val intent = Intent(this, CyclingActivity::class.java)
                val actCon = ActivityContents(activityType = "cycling")

                startActivity(intent)
            }
        }

        liftButton.setOnClickListener{
            if (isGuest) {
                // If user a guest, show the ad page
                activityType = "weightlifting"

                val intent = Intent(this, AdPage::class.java)
                intent.putExtra("activityType", activityType) // ad page goes to the intended activity after pressing continue
                intent.putExtra("isGuest", true)
                startActivity(intent)
            } else {
                // else, proceed to WeightLifting
                val intent = Intent(this, LiftingActivity::class.java)
                val actCon = ActivityContents(activityType = "weightlifting")

                startActivity(intent)
            }
        }


        yogaButton.setOnClickListener{
            if (isGuest) {
                // If user a guest, show the ad page
                activityType = "yoga"
                val intent = Intent(this, AdPage::class.java)
                intent.putExtra("activityType", activityType) // ad page goes to the intended activity after pressing continue
                intent.putExtra("isGuest", true)
                startActivity(intent)
            } else {
                // else, proceed to WalkingActivity
                val intent = Intent(this, YogaActivity::class.java)
                val actCon = ActivityContents(activityType = "yoga")

                startActivity(intent)
            }
        }

        Hiitbutton.setOnClickListener{
            if (isGuest) {
                // If user a guest, show the ad page
                activityType = "hiit"
                val intent = Intent(this, AdPage::class.java)
                intent.putExtra("activityType", activityType) // ad page goes to the intended activity after pressing continue
                intent.putExtra("isGuest", true)
                startActivity(intent)
            } else {
                // else, proceed to WalkingActivity
                val intent = Intent(this, hiit::class.java)
                val actCon = ActivityContents(activityType = "hiit")

                startActivity(intent)
            }
        }
    }
}