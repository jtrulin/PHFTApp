package com.example.phftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentValidation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_validation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cardNumber = findViewById<EditText>(R.id.enterCardNumber)
        val cardCvv = findViewById<EditText>(R.id.enterCvv)
        val cardMonth = findViewById<EditText>(R.id.enterMonth)
        val cardYear = findViewById<EditText>(R.id.enterYear)
        val payButton = findViewById<Button>(R.id.payButton)
        payButton.setOnClickListener {
            if (validateCard(cardNumber, cardCvv, cardMonth, cardYear)) {
                val intent = Intent(this, PaymentSuccess::class.java)
                startActivity(intent)
            }

        }
    }

private fun validateCard(cardNumber: EditText, cardCVV: EditText, cardMonth: EditText, cardYear: EditText): Boolean {

    // turning arguments into strings
    val cardNumber = cardNumber.text.toString()
    val cardCVV = cardCVV.text.toString()
    val cardMonth = cardMonth.text.toString()
    val cardYear = cardYear.text.toString()

    if (cardNumber.isEmpty() || cardCVV.isEmpty() || cardMonth.isEmpty() || cardYear.isEmpty()) {
        Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
        return false
    } else if (cardNumber.length != 16 || cardCVV.length != 3) {
        Toast.makeText(this, "Please enter a valid card number and cvv", Toast.LENGTH_SHORT).show()
        return false
    } else if (cardMonth.toInt() < 1 || cardMonth.toInt() > 12) {
        Toast.makeText(this, "Please enter a valid month", Toast.LENGTH_SHORT).show()
        return false
    } else if (cardYear.toString().toInt() < 1) {
        Toast.makeText(this, "Please enter a valid year", Toast.LENGTH_SHORT).show()
        return false
    } else {
        return true
    }
}
}