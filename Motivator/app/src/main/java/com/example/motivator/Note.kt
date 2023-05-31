package com.example.motivator

import androidx.room.*

@Entity(tableName = "Note")
class Note (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val note: String,
    val date: String
)