package com.example.motivator

import android.content.Context
import androidx.room.*

@Database(entities = [Note::class], version = 1)
abstract class NotesDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        fun getDb(context: Context): NotesDB{
            return Room.databaseBuilder(context.applicationContext,NotesDB::class.java,"test.db").build()
        }
    }
}