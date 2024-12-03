package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Messagetrainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enable edge-to-edge layout for the activity
        setContentView(R.layout.activity_messagetrainer)

        // Get references to the RecyclerView and UI elements
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val editTextMessage: EditText = findViewById(R.id.editTextMessage)
        val buttonSend: Button = findViewById(R.id.buttonSend)
        val backButton: Button = findViewById(R.id.backButton)

        // Set up RecyclerView with LayoutManager
        val messageList = mutableListOf<String>()  // Example message list
        val adapter = MessageAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Handle the back button click to finish the activity
        backButton.setOnClickListener {
            val intent = Intent(this, Trainer::class.java)
            startActivity(intent)
        }

        // Handling the edge-to-edge effect by applying WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            recyclerView.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle the send button click to add a message to the RecyclerView
        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            if (message.isNotEmpty()) {
                messageList.add(message)  // Add new message to list
                adapter.notifyItemInserted(messageList.size - 1)  // Notify adapter
                recyclerView.scrollToPosition(messageList.size - 1)  // Scroll to latest message
                editTextMessage.setText("")  // Clear the input field
            }
        }
    }
}


