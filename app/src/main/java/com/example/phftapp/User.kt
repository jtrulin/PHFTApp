package com.example.phftapp

// User class, each use has the same credentials
data class User(
    val name: String,
    val age: Int,
    val id: Int,
    val weight: Float,
    val height: Float,
    val email: String,
    val password: String,
    val securityAnswer: String
) {

    //val goals: String = ""

    //fun readInfo(){
       // println("Hello $name! You are $age years old, with a weight of $weight and is $height tall!")
    //}
}