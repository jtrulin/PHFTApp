package com.example.phftapp

class User {
    val name: String = ""
    val age: Int = 0
    val weight: Float = 0.0f
    val height: Float = 0.0f
    val goals: String = ""

    fun readInfo(){
        println("Hello $name! You are $age years old, with a weight of $weight and is $height tall!")
    }
}