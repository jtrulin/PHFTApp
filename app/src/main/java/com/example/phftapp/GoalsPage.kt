package com.example.phftapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GoalsPage : AppCompatActivity() {
    private lateinit var goalAdapter: ArrayAdapter<String>
    private val goalList = mutableListOf<String>() // Store goals in a list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goals_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Initializing inputs and view
        val goalInput = findViewById<EditText>(R.id.goalInput)
        val saveGoalButton = findViewById<Button>(R.id.saveGoalButton)
        val goalListView = findViewById<ListView>(R.id.goalListView)
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        //Passes in if this is a Guest on the page
        val isGuest = intent.getBooleanExtra("isGuest", false)

        // Adapter for the ListView
        goalAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, goalList)
        goalListView.adapter = goalAdapter

        // Load saved goals from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserGoals", Context.MODE_PRIVATE)
        val savedGoals = sharedPreferences.getStringSet("goals", setOf())?.toMutableSet() ?: mutableSetOf()
        goalList.addAll(savedGoals)
        goalAdapter.notifyDataSetChanged()

        saveGoalButton.setOnClickListener {
            if(isGuest){
                Toast.makeText(this, "Guests cannot save goals!", Toast.LENGTH_SHORT).show()
            }

            val goal = goalInput.text.toString().trim()
            if (goal.isNotEmpty()) {
                goalList.add(goal) // Add the new goal to the list
                goalAdapter.notifyDataSetChanged()

                // Save updated goals to SharedPreferences
                with(sharedPreferences.edit()) {
                    putStringSet("goals", goalList.toSet())
                    apply()
                }

                goalInput.text.clear() // Clear the input field
            } else {
                Toast.makeText(this, "Please enter a valid goal.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}