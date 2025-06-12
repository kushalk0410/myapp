package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.GroupDBHelper
import com.example.myapplication.database.StudentNoteAdapter

class StudentRecyclerView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_recycler_view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val regNo = intent.getStringExtra("Username") ?: return  // fixed key: should match what was put in intent
        val textViewUsername = findViewById<TextView>(R.id.textUserName)
        textViewUsername.text = "Welcome: $regNo"

        val dbHelper = GroupDBHelper(this)
        val notes = dbHelper.getGroupsForStudent(regNo) // keep only this declaration

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StudentNoteAdapter(notes, editable = false)

        Log.d("StudentRecyclerView", "Notes found for $regNo: ${notes.size}")
        for (note in notes) {
            Log.d("StudentRecyclerView", "Note: ${note.title} -> ${note.content}")
        }
    }
}
