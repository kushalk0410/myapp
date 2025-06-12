package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import com.example.myapplication.database.GroupDBHelper

class teacher_register : AppCompatActivity() {

    private lateinit var editName: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var warningText: TextView
    private lateinit var btnRegister: MaterialButton
    private lateinit var db: GroupDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_register)

        editName = findViewById(R.id.editName)
        editPassword = findViewById(R.id.editPassword)
        warningText = findViewById(R.id.warningText)
        btnRegister = findViewById(R.id.btnRegister)
        db = GroupDBHelper(this)

        editPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                warningText.visibility =
                    if (s.toString().length < 6) View.VISIBLE else View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnRegister.setOnClickListener {
            val name = editName.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
            } else {
                val success = db.insertTeacher(name, password)
                if (success) {
                    Toast.makeText(this, "Teacher registered successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, RecyclerViewActivity::class.java)
                    intent.putExtra("teacherName", name)
                    intent.putExtra("source", "register") // ✅ Set source
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Teacher already exists!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
