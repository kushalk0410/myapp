package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.database.GroupDBHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class StudentLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val regNo = findViewById<TextInputEditText>(R.id.etRegNo)
        val password = findViewById<TextInputEditText>(R.id.etStudentPassword)
        val loginButton = findViewById<MaterialButton>(R.id.btnStudentLogin)

        val dbHelper = GroupDBHelper(this)

        loginButton.setOnClickListener {
            val reg = regNo.text.toString().trim()
            val pass = password.text.toString().trim()

            Log.d("StudentLogin", "Login attempt - RegNo: $reg, Password: $pass")

            if (reg.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                Log.w("StudentLogin", "Fields are empty.")
                return@setOnClickListener
            }

            val isValid = dbHelper.verifyStudent(reg, pass)

            if (isValid) {
                Toast.makeText(this, "Student login successful", Toast.LENGTH_SHORT).show()
                Log.d("StudentLogin", "Login successful for $reg")

                val intent = Intent(this, StudentRecyclerView::class.java)
                intent.putExtra("Username", reg)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid registration number or password", Toast.LENGTH_SHORT).show()
                Log.e("StudentLogin", "Login failed for $reg")
            }
        }
    }
}
