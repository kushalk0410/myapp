package com.example.myapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myapplication.Person

class GroupDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "groups.db"
        private const val DB_VERSION = 5
        private const val TABLE_NAME = "notes"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_CONTENT = "content"
        private const val COL_TEACHER = "teacher_name"
        private const val STUDENT_TABLE = "students"
        private const val STUDENT_NAME = "name"
        private const val STUDENT_REG_NO = "reg_no"
        private const val STUDENT_PASSWORD = "password"
        private const val TEACHER_TABLE = "teachers"
        private const val TEACHER_NAME = "name"
        private const val TEACHER_ID = "id"
        private const val TEACHER_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createNotesTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT,
                $COL_CONTENT TEXT,
                $COL_TEACHER TEXT
            );
        """.trimIndent()

        val createMembersTable = """
            CREATE TABLE group_members (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                group_id INTEGER,
                reg_no TEXT,
                FOREIGN KEY(group_id) REFERENCES $TABLE_NAME(id)
            );
        """.trimIndent()

        val createStudentsTable = """
            CREATE TABLE $STUDENT_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $STUDENT_NAME TEXT,
                $STUDENT_REG_NO TEXT UNIQUE,
                $STUDENT_PASSWORD TEXT
            );
        """.trimIndent()

        val createTeachersTable = """
            CREATE TABLE $TEACHER_TABLE (
                $TEACHER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $TEACHER_NAME TEXT,
                $TEACHER_PASSWORD TEXT
            );
        """.trimIndent()

        db.execSQL(createNotesTable)
        db.execSQL(createMembersTable)
        db.execSQL(createStudentsTable)
        db.execSQL(createTeachersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS group_members")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $STUDENT_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $TEACHER_TABLE")
        onCreate(db)
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COL_ID ASC")

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT))
                val teacherName = cursor.getString(cursor.getColumnIndexOrThrow(COL_TEACHER)) // ✅ fetch teacher name

                notes.add(Note(id, title, content, teacherName)) // ✅ pass all 4 arguments
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return notes
    }

    fun getNotesByTeacher(teacherName: String): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null,
            "$COL_TEACHER = ?",
            arrayOf(teacherName), null, null,
            "$COL_ID ASC"
        )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT))
                val teacherName = cursor.getString(cursor.getColumnIndexOrThrow(COL_TEACHER))
                notes.add(Note(id, title, content,teacherName))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notes
    }

    fun insertNoteAndReturnId(note: Note): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", note.title)
            put("content", note.content)
            put("teacherName", note.teacherName)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun updateNoteContent(noteId: Int, content: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("content", content)
        }
        db.update("notes", values, "id = ?", arrayOf(noteId.toString()))
    }

    fun deleteNoteById(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
        db.delete("group_members", "group_id = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getNoteContent(noteId: Int): String? {
        val db = readableDatabase
        var content: String? = null
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COL_CONTENT),
            "$COL_ID = ?",
            arrayOf(noteId.toString()),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT))
        }
        cursor.close()
        db.close()
        return content
    }

    fun addMemberToGroup(groupId: Int, regNo: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("group_id", groupId)
            put("reg_no", regNo)
        }
        db.insert("group_members", null, values)
        db.close()
    }

    fun getMembersByGroupId(groupId: Int): List<String> {
        val members = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.query(
            "group_members",
            arrayOf("reg_no"),
            "group_id = ?",
            arrayOf(groupId.toString()),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                members.add(cursor.getString(cursor.getColumnIndexOrThrow("reg_no")))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return members
    }

    fun isStudentInGroup(groupId: Int, regNo: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT 1 FROM group_members WHERE group_id=? AND reg_no=?",
            arrayOf(groupId.toString(), regNo)
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun insertStudent(name: String, regNo: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(STUDENT_NAME, name)
            put(STUDENT_REG_NO, regNo)
            put(STUDENT_PASSWORD, password)
        }
        val result = db.insert(STUDENT_TABLE, null, values)
        if (result == -1L) {
            Log.e("DBHelper", "Failed to insert student: $regNo")
        } else {
            Log.d("DBHelper", "Student inserted: $regNo")
        }
        db.close()
        return result != -1L
    }

    fun verifyStudent(regNo: String, password: String): Boolean {
        val db = readableDatabase
        val query =
            "SELECT * FROM $STUDENT_TABLE WHERE $STUDENT_REG_NO = ? AND $STUDENT_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(regNo, password))
        val exists = cursor.moveToFirst()
        Log.d("DBHelper", "Student login attempt: $regNo | Success: $exists")
        cursor.close()
        db.close()
        return exists
    }

    fun isValidStudent(regNo: String): Boolean {
        val db = readableDatabase
        val cursor =
            db.rawQuery("SELECT 1 FROM $STUDENT_TABLE WHERE $STUDENT_REG_NO = ?", arrayOf(regNo))
        val exists = cursor.moveToFirst()
        Log.d("DBHelper", "Check if student exists: $regNo | Found: $exists")
        cursor.close()
        db.close()
        return exists
    }

    fun insertTeacher(name: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TEACHER_NAME, name)
            put(TEACHER_PASSWORD, password)
        }
        val result = db.insert(TEACHER_TABLE, null, values)
        db.close()
        return result != -1L
    }

    fun verifyTeacher(name: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TEACHER_TABLE,
            null,
            "$TEACHER_NAME = ? AND $TEACHER_PASSWORD = ?",
            arrayOf(name, password),
            null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    fun getGroupsForTeacher(teacherName: String): List<Person> {
        val groups = mutableListOf<Person>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COL_ID, COL_TITLE),
            "$COL_TEACHER = ?",
            arrayOf(teacherName),
            null, null,
            "$COL_ID ASC"
        )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
                groups.add(Person(id, title))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return groups
    }

    fun getGroupsForStudent(regNo: String): List<Note> {
        val db = readableDatabase
        val notes = mutableListOf<Note>()

        val query = """
        SELECT n.id, n.title, n.content, n.teacher_name 
        FROM notes n
        INNER JOIN group_members gm ON n.id = gm.group_id
        WHERE gm.reg_no = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(regNo))
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            val teacherName = cursor.getString(3)
            notes.add(Note(id, title, content, teacherName))
        }
        cursor.close()
        db.close()
        return notes
    }


}
