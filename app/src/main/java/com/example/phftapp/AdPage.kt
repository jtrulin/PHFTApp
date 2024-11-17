package com.example.phftapp

import android.annotation.SuppressLint
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
    lateinit var myAd : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ad_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
                startActivity(intent)
            }
            else if(activityType == "weightlifting"){
                val intent = Intent(this, LiftingActivity::class.java)
                startActivity(intent)
            }

        }

    }
}