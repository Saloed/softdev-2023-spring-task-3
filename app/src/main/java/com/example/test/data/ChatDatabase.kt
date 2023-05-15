package com.example.test.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(ChatDatabaseTypeConverters::class)
@Database(entities = [ChatElement::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun ChatElementDAO(): ChatElementDao

    companion object {
        @Volatile
        private var Instance: ChatDatabase? = null
        fun getDatabase(context: Context): ChatDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ChatDatabase::class.java, "item_database").build()
                    .also { Instance = it }
            }
        }
    }
}