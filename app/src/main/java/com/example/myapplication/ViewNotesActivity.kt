package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.AddNoteActivity
import com.example.myapplication.database.GroupDBHelper
import com.example.myapplication.database.NoteAdapter
import com.example.myapplication.database.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewNotesActivity : AppCompatActivity() {

    private lateinit var dbHelper: GroupDBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var teacherName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_notes)

        teacherName = intent.getStringExtra("teacherName") ?: run {
            Toast.makeText(this, "Teacher name missing!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        dbHelper = GroupDBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewNotes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val notes = dbHelper.getNotesByTeacher(teacherName).toMutableList()
        noteAdapter = NoteAdapter(notes,dbHelper)
        recyclerView.adapter = noteAdapter

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddNote)
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("teacherName", teacherName)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val refreshedNotes = dbHelper.getNotesByTeacher(teacherName).toMutableList()
        noteAdapter.updateNotes(refreshedNotes)
    }
}
