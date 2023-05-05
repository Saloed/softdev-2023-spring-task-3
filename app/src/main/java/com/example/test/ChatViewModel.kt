package com.example.test


import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.test.data.ChatElement
import com.example.test.data.ChatList
import com.example.test.data.ChatUiState
import com.example.test.data.IChannelMessages
import com.example.test.data.IChatList
import com.example.test.data.Message
import com.example.test.data.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception


class ChatViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()


    fun setSelectedChat(chat: ChatElement) {
        _uiState.update { currentState ->
            currentState.copy(
                isChatSelected = true,
                selectedChat = chat,
                selectedChatId = chat.id
            )
        }
        if (chat.messages.isEmpty()) { // TODO:Lazy load сообщений
            updateChatMessages(chat) // TODO: Придумать как обновить сам чат, а не только его копию в selectedChat
        }
    }

    fun updateChats() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    chatList = updateDiscord()
                )
            }
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private suspend fun updateDiscord(): ChatList {
        return withContext(Dispatchers.Default) {
            val access_token =
                userPreferencesRepository.discordToken.first() // TODO: Уточнить является ли это нормальной практикой

            val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://discord.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create()).build()
            val CL: IChatList = retrofit.create(IChatList::class.java)
            var response = ""
            try {
                response = CL.chats(access_token)

            } catch (e: Exception) {
                e.printStackTrace() // TODO: Error handling
            }
            var chats: List<ChatElement> = listOf()
            try {
                chats = json.decodeFromString<List<ChatElement>>(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext ChatList(chats)
        }
    }

    private fun updateChatMessages(chat: ChatElement) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedChat = chat.updateMessages(updateDiscordMessages(chat.id))
                currentState.copy(
                    chatList = _uiState.value.chatList.updateChatById(updatedChat),
                    isChatSelected = true,
                    selectedChat = updatedChat,
                    selectedChatId = chat.id
                )

            }
        }
    }

    private suspend fun updateDiscordMessages(chatId: String): List<Message> {

        return withContext(Dispatchers.Default) {
            val access_token =
                userPreferencesRepository.discordToken.first() // TODO: Уточнить является ли это нормальной практикой

            val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://discord.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create()).build()
            val CM: IChannelMessages = retrofit.create(IChannelMessages::class.java)
            var response = ""
            try {
                response = CM.messages(access_token, chatId)

            } catch (e: Exception) {
                e.printStackTrace() // TODO: Error handling
            }
            println(response)
            var messages: List<Message> = listOf()
            try {
                messages = json.decodeFromString<List<Message>>(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext (messages)
        }
    }
}