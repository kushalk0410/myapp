package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.GroupDBHelper

class ProfileRecyclerViewAdapter(
    private val context: Context,
    private val list: MutableList<Person>,
    private val dbHelper: GroupDBHelper
) : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileNameTextView: TextView = itemView.findViewById(R.id.noteTitle)
        val addMemberButton: Button = itemView.findViewById(R.id.AddFiles)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)
        val noteEditText: EditText = itemView.findViewById(R.id.noteEditText)
        val addStudentButton: Button = itemView.findViewById(R.id.btnAddStudent)
        val regNoEditText: EditText = itemView.findViewById(R.id.editRegNo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_view, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val person = list[position]
        holder.profileNameTextView.text = person.name

        val existingNote = dbHelper.getNoteContent(person.id)
        holder.noteEditText.setText(existingNote ?: "")

        holder.addMemberButton.setOnClickListener {
            val content = holder.noteEditText.text.toString().trim()
            if (content.isEmpty()) {
                Toast.makeText(context, "Please enter content", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.updateNoteContent(person.id, content)
                Toast.makeText(context, "Content saved", Toast.LENGTH_SHORT).show()
            }
        }

        holder.deleteButton.setOnClickListener {
            dbHelper.deleteNoteById(person.id)
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
            Toast.makeText(context, "Group deleted", Toast.LENGTH_SHORT).show()
        }

        holder.addStudentButton.setOnClickListener {
            if (holder.regNoEditText.visibility == View.GONE) {
                holder.regNoEditText.visibility = View.VISIBLE
            } else {
                val regNo = holder.regNoEditText.text.toString().trim()
                if (regNo.isEmpty()) {
                    Toast.makeText(context, "Please enter a registration number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!dbHelper.isValidStudent(regNo)) {
                    Toast.makeText(context, "Student is not registered", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (dbHelper.isStudentInGroup(person.id, regNo)) {
                    Toast.makeText(context, "Student already in this group", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dbHelper.addMemberToGroup(person.id, regNo)
                Toast.makeText(context, "Student added to group", Toast.LENGTH_SHORT).show()
                holder.regNoEditText.setText("")
                holder.regNoEditText.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
