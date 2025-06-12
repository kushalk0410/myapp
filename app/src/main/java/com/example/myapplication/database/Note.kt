package com.example.myapplication.database

data class Note(
    val id: Int,
    val title: String,
    var content: String,
    val teacherName: String
)
