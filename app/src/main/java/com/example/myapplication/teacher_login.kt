package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.database.GroupDBHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class teacher_login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_login)

        val dbHelper = GroupDBHelper(this)

        val name = findViewById<TextInputEditText>(R.id.etName)
        val password = findViewById<TextInputEditText>(R.id.etPassword)
        val loginButton = findViewById<MaterialButton>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val username = name.text.toString().trim()
            val pass = password.text.toString().trim()

            if (dbHelper.verifyTeacher(username, pass)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RecyclerViewActivity::class.java)
                intent.putExtra("teacherName", username)
                intent.putExtra("source", "login") // ✅ Set source
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
