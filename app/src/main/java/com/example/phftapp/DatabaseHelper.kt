package com.example.phftapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){


    companion object {
        private const val DB_NAME = "library_fitness.db"
        private const val DB_VERSION = 1
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
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
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

    //Validating User
    fun readUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }


    // ------------------------ aaron fix pls

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

}