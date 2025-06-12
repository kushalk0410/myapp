package com.example.myapplication.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val dbHelper: GroupDBHelper
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.noteTitle)
        val contentEditText: EditText = itemView.findViewById(R.id.noteEditText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item_view, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.titleTextView.text = note.title
        holder.contentEditText.setText(note.content)

        // 👇 Always visible — no toggle logic
        holder.contentEditText.visibility = View.VISIBLE

        // 👇 Save to DB when editing is done
        holder.contentEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val updatedContent = holder.contentEditText.text.toString().trim()
                if (updatedContent != note.content) {
                    note.content = updatedContent
                    dbHelper.updateNoteContent(note.id, updatedContent)
                }
            }
        }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: MutableList<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}
