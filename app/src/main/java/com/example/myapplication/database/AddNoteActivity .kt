package com.example.myapplication.database

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: GroupDBHelper
    private lateinit var teacherName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = GroupDBHelper(this)

        // ✅ Retrieve teacher name from Intent extras
        teacherName = intent.getStringExtra("teacherName") ?: run {
            Toast.makeText(this, "No teacher name provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Construct Note with teacherName
            val note = Note(id = 0, title = title, content = "", teacherName = teacherName)

            // ✅ Insert note into DB
            db.insertNoteAndReturnId(note,)

            Toast.makeText(this, "Note added. Tap it later to edit content.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
