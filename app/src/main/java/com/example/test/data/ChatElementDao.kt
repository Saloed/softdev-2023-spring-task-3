package com.example.test.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatElementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ChatElement)

    @Update
    suspend fun update(item: ChatElement)

    @Delete()
    suspend fun delete(item: ChatElement)

    @Query("SELECT * from Chats WHERE id = :id")
    fun getChatElement(id: String): Flow<ChatElement>

    @Query("SELECT * from Chats") // ORDER BY id
    fun getAllChatElements():Flow<List<ChatElement>>
}