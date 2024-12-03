package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

import com.google.android.gms.ads.MobileAds


class AdPage : AppCompatActivity() {
    private lateinit var myAd : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ad_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //This is just to make the back button work, lol
        val userId = intent.getIntExtra("userId", -1)
        /*
        if (userId == -1) {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
            finish() // Exit if no userId is found
        }*/
        val isGuest = intent.getBooleanExtra("isGuest", false)

        // initializing ad on page
        MobileAds.initialize(this@AdPage)
        myAd = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        myAd.loadAd(adRequest)

        // retrieves the type of activity you're currently on
        val activityType = intent.getStringExtra("activityType")
        val cont = findViewById<Button>(R.id.continueButton)

        cont.setOnClickListener{
            if(activityType == "running"){
                val intent = Intent(this, RunningActivity::class.java)
                if(isGuest){
                    intent.putExtra("isGuest", true)
                } else {
                    intent.putExtra("userId", userId) // Pass userId with correct key casing
                }
                startActivity(intent)
            } else if(activityType == "weightlifting"){
                val intent = Intent(this, LiftingActivity::class.java)
                if(isGuest){
                    intent.putExtra("isGuest", true)
                } else {
                    intent.putExtra("userId", userId) // Pass userId with correct key casing
                }
                startActivity(intent)
            } else if(activityType == "walking"){
                val intent = Intent(this, WalkingActivity::class.java)
                if(isGuest){
                    intent.putExtra("isGuest", true)
                } else {
                    intent.putExtra("userId", userId) // Pass userId with correct key casing
                }
                startActivity(intent)
            } else if(activityType == "cycling"){
                val intent = Intent(this, CyclingActivity::class.java)
                if(isGuest){
                    intent.putExtra("isGuest", true)
                } else {
                    intent.putExtra("userId", userId) // Pass userId with correct key casing
                }
                startActivity(intent)
            } else if (activityType == "yoga"){
                val intent = Intent(this, YogaActivity::class.java)
                if(isGuest){
                    intent.putExtra("isGuest", true)
                } else {
                    intent.putExtra("userId", userId) // Pass userId with correct key casing
                }
                startActivity(intent)
            } else if (activityType == "hiit"){
                val intent = Intent(this, hiit::class.java)
                if(isGuest){
                    intent.putExtra("isGuest", true)
                } else {
                    intent.putExtra("userId", userId) // Pass userId with correct key casing
                }
                startActivity(intent)
            }
        }
    }
}