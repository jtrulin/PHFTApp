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
) {

    //val goals: String = ""

    //fun readInfo(){
       // println("Hello $name! You are $age years old, with a weight of $weight and is $height tall!")
    //}
}