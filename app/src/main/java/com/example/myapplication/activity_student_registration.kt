package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.database.GroupDBHelper

class activity_student_registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_registration)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize database
        val dbHelper = GroupDBHelper(this)

        // Link UI elements
        val nameField = findViewById<EditText>(R.id.nameField)
        val regNoField = findViewById<EditText>(R.id.regNoField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val submitButton = findViewById<Button>(R.id.submitStudentBtn)

        // Handle button click
        submitButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val regNo = regNoField.text.toString().trim()
            val password = passwordField.text.toString()

            when {
                name.isEmpty() -> nameField.error = "Name is required"
                regNo.isEmpty() -> regNoField.error = "Registration No. is required"
                password.isEmpty() -> passwordField.error = "Password is required"
                password.length < 6 -> passwordField.error = "Password must be at least 6 characters"
                else -> {
                    val success = dbHelper.insertStudent(name, regNo, password)
                    if (success) {
                        Toast.makeText(this, "Student registered successfully!", Toast.LENGTH_SHORT).show()
                        nameField.text.clear()
                        regNoField.text.clear()
                        passwordField.text.clear()
                    } else {
                        Toast.makeText(this, "Registration failed! Reg No. may already exist.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
