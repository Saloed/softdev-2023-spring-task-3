package com.example.test.data

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.text.SimpleDateFormat
import java.time.Instant


@SuppressLint("SimpleDateFormat")
@Entity(tableName = "messages")
@Serializable
data class Message(
    @PrimaryKey
    var content: String,

    @Ignore //TODO: добавить TypeConverter
    @SerialName("author") val author: Author
) {


    @Transient
    var sender: String = author.username

    @Transient
    var previewBitmap: ByteArray? = null

    @Transient
    var imagePath: String? =
        null

    @Transient
    var chatId: String? = null

    @Ignore
    @SerialName("timestamp")
    private val timestampISO: String = ""

    @Transient
    var timestamp: Long = 0

    init {

        if (timestampISO != ""&&timestamp==0L) {
            val date =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS+hh:mm").parse(timestampISO)
            if (date != null) {
                timestamp = date.time / 1000
            }
        }
    }

    //DAO constructor
    constructor(content: String, chatId: String) : this(content, Author("", "")) {
        this.chatId = chatId
    }
}

@Serializable
data class Author(val username: String, val id: String)