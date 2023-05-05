package com.example.test.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val content: String,
    @SerialName("author") val author: Author
) {
    @Transient
    val sender: String = author.username

}

@Serializable
data class Author(val username: String, val id: String)