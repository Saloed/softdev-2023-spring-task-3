package com.example.test.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ChatElement(
    val id: String,
    @Transient var name: String = "",
    @Transient val previewImage: String = "", // TODO: Не обязательно картинка же
    @Transient var displayMessage: String = "",
    @Transient val messages: List<Message> = listOf(),
    @SerialName("recipients") val recipients: List<RecipientsData>
) {
    init {
        this.name = recipients.map { it.username }.fold("") { s, name -> s + name }
    }

    fun updateMessages(messages: List<Message>): ChatElement { // TODO: Ну плохо наверное
        return ChatElement(id, name, previewImage, if(messages.isEmpty()) "" else messages.first().content , messages, recipients)
    }
}


@Serializable
data class RecipientsData(
    @SerialName("username") val username: String
)
