package com.example.phftapp

// User class, each use has the same credentials
data class User(
    val name: String = "Bob",
    val age: Int = 20,
    val id: Int = 123096,
    val weight: Float = 120.5f,
    val height: Float = 5.8f,
    val email: String = "rafe@gmail.com",
    val password: String = "rafe@123",
    val securityAnswer: String = "uta"
)

data class Payment(
    val cardNumber: String,
    val cvv: Int,
    val month: Int,
    val year: Int,
    val userId: Int
)

data class ProgressReport(
    val caloriesBurned: Double,
    val userId: Int
)