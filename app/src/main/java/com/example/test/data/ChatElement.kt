package com.example.test.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.test.td.Example
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
@Entity(tableName="Chats")
class ChatElement(
    @PrimaryKey override var id: String,
    @Transient override var name: String = "",
    @Transient override var previewImage: String = "", // TODO: Не обязательно картинка же
    @Transient override var displayMessage: String = "",
    @SerialName("recipients") @Ignore override val recipients: List<RecipientsData> = listOf()
) :IChatElement {
    @Transient
    @Ignore
    var _messages: SnapshotStateList<Message> = mutableStateListOf()
    constructor(
        id: String, name: String,
        previewImage: String,
        displayMessage: String
    ) : this(id, name, previewImage, displayMessage,listOf())
    constructor(
        id: String, name: String,
        previewImage: String,
        displayMessage: String,
        messages: List<Message>,
        recipients: List<RecipientsData>
    ) : this(id, name, previewImage, displayMessage, recipients) {
        this.messages = messages
    }

    @Transient
    override var messages
        get() = _messages.toList()
        set(value: List<Message>) {
            _messages.addAll(value)
        }

    init {
        if (name == "") {
            this.name = recipients.map { it.username }.fold("") { s, name -> s + name }
        }
        this.messages = messages
    }

    fun updateMessagesOld(messages: List<Message>): ChatElement { // TODO: Ну плохо наверное
        return ChatElement(
            id,
            name,
            previewImage,
            if (messages.isEmpty()) "" else messages.first().content,
            messages,
            recipients
        )
    }

    override fun updateMessages() {
        Example.getMessages(this.id) { updateCallback(it) }
    }

    private fun updateCallback(messages: List<Message>): String {
        this.messages = messages
        return ""
    }
}


@Serializable
data class RecipientsData(
    @SerialName("username") val username: String
)
