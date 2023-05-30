package com.example.test.data

import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi

interface ChatRepository {
    fun getAllChatsStream(): Flow<List<ChatElement>>
    fun getChatStream(id: String): Flow<ChatElement>
    suspend fun insertChat(item: ChatElement)
    suspend fun deleteChat(item: ChatElement)
    suspend fun updateChat(item: ChatElement)

}