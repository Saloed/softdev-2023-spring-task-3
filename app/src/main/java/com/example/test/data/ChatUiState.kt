package com.example.test.data

data class ChatUiState(
    val isChatSelected: Boolean = false,
    val selectedChatId: String = "",
    val selectedChatIndex: Int = 0,
    val selectedChat: ChatElement? = null,
//    val selectedChatList:ChatList? = null // заготовка под списки чатов
    val chatList: ChatList = ChatList(listOf())
){
    fun ChatUiState.toChatList():ChatList = chatList
    fun ChatList.toChatUiState(actionEnabled:Boolean=false):ChatUiState = ChatUiState(chatList=chatList)


}
