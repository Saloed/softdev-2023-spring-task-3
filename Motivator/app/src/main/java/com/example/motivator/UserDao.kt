package com.example.motivator

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun addNote(note: Note)

    @Query("Delete from note where id = :i")
    fun deleteNote(i: Int)

    @Query("Select * from Note")
    fun getAllNotes(): Flow<List<Note>>
}