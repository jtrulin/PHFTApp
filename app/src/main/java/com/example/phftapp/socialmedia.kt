package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class socialmedia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_socialmedia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.socialmediaID)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backButton = findViewById<Button>(R.id.backButton)
        val userId = intent.getIntExtra("userId", -1)

        backButton.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }
}