package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterPage : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityRegisterBinding.inflate
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)

        databaseHelper = DatabaseHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // retrieving user input
        val userName = findViewById<EditText>(R.id.enterName)
        val userAge = findViewById<EditText>(R.id.enterAge)
        val userID = findViewById<EditText>(R.id.enterUseId)
        val userWeight = findViewById<EditText>(R.id.enterWeight)
        val userHeight = findViewById<EditText>(R.id.enterHeight)
        val userEmail = findViewById<EditText>(R.id.regEmail)
        val userPassword = findViewById<EditText>(R.id.regPassword)
        val userSecurityAnswer = findViewById<EditText>(R.id.enterAnswer)
        val registerButton = findViewById<Button>(R.id.rButton)


        val newSpinner = findViewById<Spinner>(R.id.newSpinner)


// List of security questions
        val questions = listOf(
            "Select Security Question",
            "What is your mother's maiden name?",
            "What was the name of your first pet?",
            "In what city were you born?",
            "What was your first car?",
            "What is your favorite book?"
        )

// Custom ArrayAdapter for the Spinner
        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, questions) {
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
        newSpinner.adapter = adapter

        // Handle Spinner item selection
        newSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) { // Ignore the default option
                    val selectedQuestion = questions[position]
                    Toast.makeText(this@RegisterPage, "Selected: $selectedQuestion", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        registerButton.setOnClickListener {
            if (validateCredentials(userEmail, userPassword, userID)) {

                // creating a User instance per registered user and saves to database.
                // when users log in regularly, we should search through database to confirm they're
                // in our system
                val name = userName.text.toString()
                val age = userAge.text.toString().toInt()
                val id = userID.text.toString().toInt()
                val weight = userWeight.text.toString().toFloat()
                val height = userHeight.text.toString().toFloat()
                val email = userEmail.text.toString()
                val password = userPassword.text.toString()
                val securityAnswer = userSecurityAnswer.text.toString()

                //val user = User(name, age, id, email) // checking if class is working (it does)
                val insertedId = databaseHelper.insertUser(User(name, age, id, weight, height, email, password, securityAnswer))

                if (insertedId == -1L) {
                    //Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginPage::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateCredentials(userEmail: EditText, userPassword: EditText, userID: EditText): Boolean { // validEmail and validPassword are of type "EditText"

        // turning arguments into strings
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()
        val id = userID.text.toString()

        val atPosition = email.indexOf('@') // helps finding the '.' AFTER '@'. if returned true then gives position, if not it returns -1

        if (email.isEmpty() || email.indexOf('@') == -1 || email.indexOf('.', atPosition) == -1) { // checks if email is valid
            // run when email is invalid
            Toast.makeText(this, "Cannot login! Invalid email", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.isEmpty() || password.length <= 7) { // checking password length
            // run when password is invalid
            Toast.makeText(this, "Cannot login! At least 8 characters long!", Toast.LENGTH_SHORT).show()
            return false
        } else if (!password.contains("[0-9]".toRegex())) { // checking if password contains at least 1 number, using regular expression
            // run when password is invalid
            Toast.makeText(this, "Cannot login! At least 1 number!", Toast.LENGTH_SHORT).show()
            return false
        } else if (!password.contains("[!\"#$%&'()*+,-./:;<=>?@^_`{|}~]".toRegex())) { // checking if password contains at least 1 special character, using regular expression
            // run when password is invalid
            Toast.makeText(this, "Cannot login! At least 1 special character!", Toast.LENGTH_SHORT).show()
            return false
        } else if (id.length <= 5) { // checks user length, must be 6 or more characters
            // run when password is invalid
            Toast.makeText(this, "Cannot login! UserID must be at least 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        } else if (id.contains("[!\"#$%&'()*+,-./:;<=>?@^_`{|}~]".toRegex())) { // checks if userID has special characters, which is not allowed
            // run when password is invalid
            Toast.makeText(this, "Cannot login! UserID must not have special characters!", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true // returns true only if conditions are met
        }
    }



// this should store stuff into a database
    /*private fun writeToFile(fileName: String, content: String) {
        val path = applicationContext.filesDir
        val file = File(path, fileName)
        FileOutputStream(file).use { writer ->
            writer.write(content.toByteArray())
        }
        Toast.makeText(this, "Put email in file!", Toast.LENGTH_SHORT).show()

    }
    */
}
