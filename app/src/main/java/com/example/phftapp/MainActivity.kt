package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }

        val guestButton = findViewById<Button>(R.id.gButton)
        val loginButton = findViewById<Button>(R.id.lButton)


        loginButton.setOnClickListener {
            // run when button is clicked
            Toast.makeText(this, "Login button clicked!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        guestButton.setOnClickListener {
            // run when button is clicked
            Toast.makeText(this, "Guest button clicked!", Toast.LENGTH_SHORT).show()
        }


    }
}