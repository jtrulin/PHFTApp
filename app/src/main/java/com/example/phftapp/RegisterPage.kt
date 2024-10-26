package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)
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
        val securityAnswer = findViewById<EditText>(R.id.enterAnswer)
        val registerButton = findViewById<Button>(R.id.rButton)

        // setting up security questions with the spinner
        val securityQuestionsSpinner= findViewById<Spinner>(R.id.spinner)
        val listQuestions = listOf("Select Security Question", "In what city were you born?", "First Pet Name?")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listQuestions)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        securityQuestionsSpinner.adapter = arrayAdapter

        securityQuestionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedQuestion = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@RegisterPage, "You selected '$selectedQuestion'", Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        registerButton.setOnClickListener {
            if (validateCredentials(userEmail, userPassword, userID)) {
                // run when login is successful
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                // creating a User instance per registered user and saves to database.
                // when users log in regularly, we should search through database to confirm they're
                // in our system
                val name = userName.text.toString()
                val age = userAge.text.toString().toInt()
                val weight = userWeight.text.toString().toFloat()
                val height = userHeight.text.toString().toFloat()

                val user = User(name, age, weight, height) // checking if class is working (it does)

                // debugging
                Toast.makeText(this, "User Created: $user", Toast.LENGTH_SHORT).show()


                // later, store email and password to a database, so when user logs in, their credentials should be searched in the database
                //val storeEmail = validEmail.text.toString()
                //writeToFile("emails.txt", storeEmail)


                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)

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
        }
        else {
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

