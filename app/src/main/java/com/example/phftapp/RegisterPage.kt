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

   // private lateinit var binding: Activity
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

        // setting up security questions with the spinner
        // setting up security questions with the spinner
        // setting up security questions with the spinner
        val securityQuestionsSpinner = findViewById<Spinner>(R.id.spinner)
        val listQuestions = listOf(
            "Select Security Question",
            "What is your mother's maiden name?",
            "What was the name of your first pet?",
            "In what city were you born?",
            "What was your first car?",
            "What is your favorite book?"
        )

        // Custom ArrayAdapter to set main Spinner text to white and drop-down items to black
        val arrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, listQuestions) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                // Set color for the main view of the Spinner (shown when closed) to white
                (view as? TextView)?.setTextColor(resources.getColor(R.color.white, null))

                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)

                // Set color for drop-down items to black
                (view as? TextView)?.setTextColor(resources.getColor(R.color.black, null))

                return view
            }
        }

// Apply the custom adapter to the Spinner
        securityQuestionsSpinner.adapter = arrayAdapter


// Apply the adapter to the Spinner
        securityQuestionsSpinner.adapter = arrayAdapter


// Apply the adapter to the Spinner
        securityQuestionsSpinner.adapter = arrayAdapter



        securityQuestionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedQuestion = parent.getItemAtPosition(position).toString()

                // Ignore if the default item is selected
                if (selectedQuestion != "Select Security Question") {
                    Toast.makeText(this@RegisterPage, "You selected '$selectedQuestion'", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed here
            }
        }



        registerButton.setOnClickListener {
            if (validateCredentials(userEmail, userPassword, userID)) {
                // run when login is successful
                //Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

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
                    Toast.makeText(this, "Registration failed! Error: $insertedId", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginPage::class.java)
                    startActivity(intent)
                    finish()
                }
                //val intent = Intent(this, RegisterPage::class.java)
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
            Toast.makeText(this, "Cannot login! UserID must be not have special characters!", Toast.LENGTH_SHORT).show()
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
