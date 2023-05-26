package com.example.test.data

import kotlinx.serialization.Serializable

@Serializable
class ChatList(
    val chats: List<ChatElement>
) {
    fun updateChatById(chat: ChatElement) =
        ChatList(chats.filter { it.id != chat.id }.toMutableList() + chat)
    fun updateChatById(id:String, message:Message){
        chats.find { it.id==id }?.addMessage(message) ?: throw IndexOutOfBoundsException() // TODO: Может выбросить исключение если пришло уведомление на еще не загруженный чат
    }

    operator fun plus(other: ChatList) =
        ChatList(chats + other.chats)

}