package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.database.GroupDBHelper

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameEditText = findViewById<EditText>(R.id.etName)
        val regNoEditText = findViewById<EditText>(R.id.etRegNo)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)

        val dbHelper = GroupDBHelper(this)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val regNo = regNoEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || regNo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertStudent(name, regNo, password)
                if (success) {
                    Toast.makeText(this, "Student Registered", Toast.LENGTH_SHORT).show()
                    finish() // go back or close screen
                } else {
                    Toast.makeText(this, "Registration failed: RegNo may already exist", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
