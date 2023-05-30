package com.example.test.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Message)

    @Update
    suspend fun update(item: Message)

    @Delete()
    suspend fun delete(item: Message)

    @Query("SELECT * from messages WHERE chatId = :id")
    fun getMessagesByChatId(id: String): Flow<List<Message>>

    @Query("SELECT * from messages") // ORDER BY id
    fun getAllMessages(): Flow<List<Message>>
}