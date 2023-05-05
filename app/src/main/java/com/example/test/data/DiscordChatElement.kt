package com.example.test.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class DiscordChatElement(
    @SerialName("id")  val id: String,
    @Transient  val name: String="",
    @Transient val previewImage: String? = null, // TODO: Не обязательно картинка же
    @Transient val displayMessage: String? = null,
    @Transient val messages: List<Message> = listOf()
)  {

}
