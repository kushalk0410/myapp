package com.example.myapplication.database

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class StudentNoteAdapter(
    private val notes: List<Note>,
    private val editable: Boolean
) : RecyclerView.Adapter<StudentNoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.noteTitle)
        val teacherNameTextView: TextView = itemView.findViewById(R.id.teacherNameTextView)
        val contentEditText: EditText = itemView.findViewById(R.id.noteEditText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.titleTextView.text = note.title
        holder.teacherNameTextView.text = "By: ${note.teacherName}"
        holder.contentEditText.setText(note.content)

        // Disable editing if not editable
        holder.contentEditText.isEnabled = editable
    }

    override fun getItemCount(): Int = notes.size
}
