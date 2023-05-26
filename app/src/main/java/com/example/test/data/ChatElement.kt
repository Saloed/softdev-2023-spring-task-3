package com.example.test.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.test.td.Example
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
@Entity(tableName = "Chats")
class ChatElement(
    @PrimaryKey var id: String,
    @Transient var name: String = "",
    @Transient var previewImage: String = "",
    @Transient var displayMessage: String = "",
    @SerialName("recipients") @Ignore var recipients: List<RecipientsData> = listOf()

) {
    @Transient
    @Ignore
    var _messages: SnapshotStateList<Message> = mutableStateListOf()

    @Transient
    @Ignore
    var unreadCount: MutableState<Int> = mutableStateOf(0)


    @Transient
    @Ignore
    private val MIN_MESSAGE_COUNT = 10

    @Transient
    var type: ChatType = ChatType.DISCORD

    constructor(
        id: String, name: String,
        previewImage: String,
        displayMessage: String
    ) : this(id, name, previewImage, displayMessage, listOf())

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
    var messages
        get() = _messages.toList()
        set(value: List<Message>) {

            for (message in value) {
                if (message !in _messages) _messages.add(message)
            }
            _messages.sortByDescending { it.timestamp }



        }

    init {
        if (name == "") {
            this.name = recipients.map { it.username }.fold("") { s, name -> s + name }
        }
        this.messages = messages
    }

    fun updateMessagesCopying(messages: List<Message>): ChatElement {
        return ChatElement(
            id,
            name,
            previewImage,
            if (messages.isEmpty()) "" else messages.first().content,
            messages,
            recipients
        )
    }

    fun setChatMessages(messages: List<Message>) {
        this.messages = messages
        this.displayMessage = messages.lastOrNull()?.content ?: ""
    }

    fun addMessage(message: Message) {
        if (message !in _messages) {

            _messages.add(0, message)

            unreadCount.value++
        }
    }

    fun updateMessages() {
        Example.getMessages(this.id) { updateCallback(it) }
    }

    private fun updateCallback(messages: List<Message>): String {
//        this.messages = messages

        for (message in messages.sortedBy { it.timestamp }.reversed()) {
            if (message !in _messages) _messages.add(message)
        }
        this.displayMessage =
            _messages.firstOrNull()?.content ?: "" // Использование setChatMessages не работает

        if (_messages.size < MIN_MESSAGE_COUNT) updateMessages() // TODO: может быть бесконечный цикл

        return ""
    }

    fun setRecipientsData(recipients: List<RecipientsData>) {
        this.recipients = recipients
    }
}


@Serializable
data class RecipientsData(
    @SerialName("username") val username: String,
    val id: String
)
