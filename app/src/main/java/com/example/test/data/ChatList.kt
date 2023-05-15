package com.example.test.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ChatList(
    val chats: List<ChatElement>// TODO:ObservableList
) {
    fun updateChatById(chat: ChatElement) =
        ChatList(chats.filter { it.id != chat.id }.toMutableList() + chat)

    operator fun plus(other: ChatList) =
        ChatList(chats + other.chats)

}