package com.example.test.data

import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getAllMessages(): Flow<List<Message>>
    fun getMessagesByChatId(id:String): Flow<List<Message>>
    suspend fun insertMessage(msg:Message)
    suspend fun deleteMessage(msg: Message)
    suspend fun updateMessage(msg: Message)
}