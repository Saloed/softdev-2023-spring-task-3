package com.example.test.data

interface IChatElement {
    var id: String
    var name: String
    var previewImage: String
    var displayMessage: String
    val recipients: List<RecipientsData>
    var messages:List<Message>

    fun updateMessages()
}