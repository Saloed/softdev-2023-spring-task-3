package com.example.test.data

import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi.Chat

class OfflineChatRepository(private val chatElementsDao: ChatElementDao) : ChatRepository {
    override fun getAllChatsStream(): Flow<List<ChatElement>> = chatElementsDao.getAllChatElements()

    override fun getChatStream(id: String): Flow<ChatElement> = chatElementsDao.getChatElement(id)

    override suspend fun insertChat(item: ChatElement) = chatElementsDao.insert(item)

    override suspend fun deleteChat(item: ChatElement) = chatElementsDao.delete(item)

    override suspend fun updateChat(item: ChatElement) = chatElementsDao.update(item)


}