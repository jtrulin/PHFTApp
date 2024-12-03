package com.example.phftapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){


    companion object {
        private const val DB_NAME = "library_fitness.db"
        private const val DB_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE users (name Text," +
                "age Integer," +
                "id Integer Primary Key," +
                "weight Real," +
                "height Real," +
                "email Text," +
                "password Text," +
                "securityAnswer Text)"
        )

        db?.execSQL("CREATE TABLE payment (cardNumber Text Primary Key," +
                "cvv Integer," +
                "month Integer," +
                "year Integer,"+
                "user_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES users(id))"
        )
        db?.execSQL("CREATE TABLE progressReport (calories_burned Real," +
                "user_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES users(id))"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS payment")
        db?.execSQL("DROP TABLE IF EXISTS progressReport")
        onCreate(db)
    }

    //Inserting User
    fun insertUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("age", user.age)
            put("id", user.id)
            put("weight", user.weight)
            put("height", user.height)
            put("email", user.email)
            put("password", user.password)
            put("securityAnswer", user.securityAnswer)
        }
        val insertId = db.insert("users", null, values)
        return insertId
    }

    //Inserting Card
    fun insertCard(payment: Payment): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("cardNumber", payment.cardNumber)
            put("cvv", payment.cvv)
            put("month", payment.month)
            put("year", payment.year)
            put("user_id", payment.userId)
        }
        val insertId = db.insert("payment", null, values)
        return insertId
    }

    //Inserting Progress Report
    fun insertProgress(progressReport: ProgressReport): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("calories_burned", progressReport.caloriesBurned)
            put("user_id", progressReport.userId)
        }
        val insertId = db.insert("progressReport", null, values)
        return insertId
    }

    //Validating User
    fun readUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }


    fun getUserName(email: String, password: String): String? {
        val db = readableDatabase
        val query = "SELECT name FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        }
        cursor.close()
        return userName
    }

    //Helps fetch UserId based on Username and Password
    fun getUserId(email: String, password: String): Int? {
        val db = readableDatabase
        val query = "SELECT id FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        var userId: Int? = null
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        return userId
    }

    fun getUserNameById(userId: Int): String? {
        val db = readableDatabase
        val query = "SELECT name FROM users WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        }
        cursor.close()
        return userName
    }

}