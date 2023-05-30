package com.example.test.data

import com.example.test.ui.messageDisplay
import kotlinx.coroutines.flow.Flow

class OfflineMessageRepository(private val messageDao: MessageDao) : MessageRepository {
    override fun getAllMessages(): Flow<List<Message>> =
        messageDao.getAllMessages()


    override fun getMessagesByChatId(id: String): Flow<List<Message>> =
        messageDao.getMessagesByChatId(id)

    override suspend fun insertMessage(msg: Message) = messageDao.insert(msg)
    override suspend fun deleteMessage(msg: Message) = messageDao.delete(msg)
    override suspend fun updateMessage(msg: Message) = messageDao.update(msg)
}