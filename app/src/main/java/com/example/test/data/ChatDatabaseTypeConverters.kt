package com.example.test.data

import androidx.room.TypeConverter

class ChatDatabaseTypeConverters {
    @TypeConverter
    fun fromRecipients(username: String): RecipientsData {
        return RecipientsData(username)
    }

    @TypeConverter
    fun usernameToRecipients(r: RecipientsData): String {
        return r.username
    }
}