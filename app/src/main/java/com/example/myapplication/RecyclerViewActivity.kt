package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.AddNoteActivity
import com.example.myapplication.database.GroupDBHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var dbHelper: GroupDBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var profileAdapter: ProfileRecyclerViewAdapter
    private lateinit var goBackButton: Button

    private var teacherName: String = ""
    private var source: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        // ✅ Get teacher name and source from Intent
        teacherName = intent.getStringExtra("teacherName") ?: run {
            finish()
            return
        }

        source = intent.getStringExtra("source") ?: ""

        // ✅ Initialize Views
        recyclerView = findViewById(R.id.recyclerView)
        fabAddNote = findViewById(R.id.fabAddNote)
        goBackButton = findViewById(R.id.btnGoBack)

        dbHelper = GroupDBHelper(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadGroupList()

        fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("teacherName", teacherName)
            startActivity(intent)
        }

        goBackButton.setOnClickListener {
            when (source) {
                "login" -> startActivity(Intent(this, teacher_login::class.java))
                "register" -> startActivity(Intent(this, teacher_register::class.java))
            }
            finish()
        }
    }

    private fun loadGroupList() {
        val groupList = dbHelper.getGroupsForTeacher(teacherName)
        profileAdapter = ProfileRecyclerViewAdapter(this, groupList.toMutableList(), dbHelper)
        recyclerView.adapter = profileAdapter
    }

    override fun onResume() {
        super.onResume()
        loadGroupList()
    }

}
