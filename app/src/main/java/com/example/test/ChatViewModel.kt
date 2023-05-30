package com.example.test


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.ChatElement
import com.example.test.data.ChatList
import com.example.test.data.ChatRepository
import com.example.test.data.ChatType
import com.example.test.data.ChatUiState
import com.example.test.data.IChannelMessages
import com.example.test.data.IChatList
import com.example.test.data.Message
import com.example.test.data.OfflineMessageRepository
import com.example.test.data.RecipientsData
import com.example.test.data.UserPreferencesRepository
import com.example.test.td.TdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ChatViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: OfflineMessageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()


    fun setSelectedChat(chat: ChatElement) {
        if (chat.messages.isEmpty()) {
            viewModelScope.launch {
                if (chat.messages.isEmpty()) { // К моменту запуска корутины сообщения уже могли появиться
                    chat.setChatMessages(messageRepository.getMessagesByChatId(chat.id).first())
                }
            }
        }
        when (chat.type) {
            ChatType.DISCORD ->
                viewModelScope.launch {
                    chat.setChatMessages(updateDiscordMessages(chat.id))
                }

            ChatType.TELEGRAM -> chat.updateMessages()
            ChatType.MIXED -> null
        }
        _uiState.update { currentState ->

            currentState.copy(
                isChatSelected = true,
                selectedChat = chat,
                selectedChatId = chat.id
            )
        }

        chat.unreadCount.value = 0


    }

    fun saveChats() {
        viewModelScope.launch {
            for (chat in _uiState.value.chatList.chats) {
                chatRepository.insertChat(chat)
                for (message in chat.messages) {
                    messageRepository.insertMessage(message)
                }
            }
        }
    }

    fun saveMessage(message: Message) {
        viewModelScope.launch {
            messageRepository.insertMessage(message)
        }
    }

    fun loadChats() {
        viewModelScope.launch {
            _uiState.update { currentState ->

                val chatList = ChatList(chatRepository.getAllChatsStream().first())
                currentState.copy(
                    chatList = chatList
                )

            }

        }


    }

    fun updateChats() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                try {
                    currentState.copy(
                        chatList = updateTelegramChats() + updateDiscord()
                    )
                } catch (e: IllegalAccessError) {
                    e.printStackTrace()// TODO: Вывести сообщение об отсутствии авторизации
                    currentState
                }
            }
            saveChats()
        }

    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private suspend fun updateDiscord(): ChatList {
        return withContext(Dispatchers.Default) {
            val access_token =
                userPreferencesRepository.discordToken.first()

            val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://discord.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create()).build()
            val CL: IChatList = retrofit.create(IChatList::class.java)
            var response = ""
            try {
                response = CL.chats(access_token)

            } catch (_: Exception) {

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
        updateTelegramMessages(chat.id)
        viewModelScope.launch {
            _uiState.update { currentState ->
                chat.updateMessages()
                currentState.copy(
                    chatList = _uiState.value.chatList.updateChatById(chat),
                    isChatSelected = true,
                    selectedChat = chat,
                    selectedChatId = chat.id
                )

            }
        }
    }

    fun getMeDiscord() {
        viewModelScope.launch {
            userPreferencesRepository.saveDiscordIdPreference(getMeDiscordRequest().id)
        }
    }

    fun getMeCallback(me: RecipientsData): String {
        viewModelScope.launch {
            userPreferencesRepository.saveTelegramIdPreference(me.id)
        }
        return ""
    }

    private suspend fun getMeDiscordRequest(): RecipientsData {
        return withContext(Dispatchers.Default) {
            val access_token =
                userPreferencesRepository.discordToken.first()

            val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://discord.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create()).build()
            val CM: IChannelMessages = retrofit.create(IChannelMessages::class.java)
            var response = ""
            try {
                response = CM.me(access_token)

            } catch (_: Exception) {

            }
            var me = RecipientsData("", "")
            try {
                me = json.decodeFromString(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext (me)
        }
    }

    private suspend fun updateDiscordMessages(chatId: String): List<Message> {

        return withContext(Dispatchers.Default) {
            val access_token =
                userPreferencesRepository.discordToken.first()

            val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://discord.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create()).build()
            val CM: IChannelMessages = retrofit.create(IChannelMessages::class.java)
            var response = ""
            try {
                response = CM.messages(access_token, chatId)

            } catch (_: Exception) {

            }
            var messages: List<Message> = listOf()
            try {
                messages = json.decodeFromString<List<Message>>(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            for (message in messages) {
                message.chatId = chatId
            }
            return@withContext (messages)
        }
    }

    fun updateTelegramChats(): ChatList {
        val chatList = TdClient.getChats { onUpdateFullChat(it) }
        return chatList
    }

    private fun onUpdateFullChat(recipients: List<RecipientsData>): String {
        _uiState.update { currentState ->
            val chat = _uiState.value.selectedChat!!
            chat.recipients = recipients
            currentState.copy(selectedChat = chat)
        }
        return ""
    }


    fun telegramInit(dbPath: String) {
        TdClient.main(dbPath) { prompt -> runBlocking { telegramLoginCallback(prompt) } }
        TdClient.subscribeOnNewMessages { id, message -> newMessageCallback(id, message) }
    }

    fun onTelegramPromptUpdate(result: String) {
        TdClient.getMe { getMeCallback(it) }
        telegramPromptResult = result
    }

    private var telegramPromptResult = ""
    private suspend fun telegramLoginCallback(prompt: String): String {
        return withContext(Dispatchers.Default) {
            while (telegramPromptResult == "") {
            }
            val out = telegramPromptResult
            telegramPromptResult = ""
            return@withContext out
        }

    }

    fun updateTelegramMessages(chatId: String) {
        TdClient.getMessages(chatId) { onTelegramChatUpdate(it) }
    }

//    fun onTelegramChatRecipientsUpdate(recipients: List<RecipientsData>) { // TODO: Не используется
//        _uiState.value.selectedChat!!.setRecipientsData(recipients)
//
//        viewModelScope.launch {
//            _uiState.update { currentState ->
//                currentState.copy()
//            }
//        }
//    }

    fun onTelegramChatUpdate(messages: List<Message>): String {
        for (message in messages) {
            _uiState.value.chatList.addMessageById(message.chatId!!, message)
        }

        return ""
    }

    fun sendMessage(message: String) {
        val chat: ChatElement =
            _uiState.value.selectedChat ?: throw IllegalStateException() // unreachable (?)
        when (chat.type) {
            ChatType.TELEGRAM -> {

                TdClient.sendMessageTd(
                    chat.id.toLongOrNull() ?: throw NullPointerException(),
                    message
                )

            }

            ChatType.DISCORD -> {
                viewModelScope.launch {
                    sendDiscordMessage(chat.id, message)
                }
            }

            else -> {}
        }

    }

    private suspend fun sendDiscordMessage(chatId: String, content: String) {
        val access_token =
            userPreferencesRepository.discordToken.first()

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://discord.com/api/")
            .addConverterFactory(ScalarsConverterFactory.create()).build()
        val CM: IChannelMessages = retrofit.create(IChannelMessages::class.java)
        try {
            CM.sendMessage(access_token, chatId, "{\"content\": \"$content\"}")

        } catch (_: Exception) {
        }
    }

    fun newMessageCallback(
        chatId: String,
        message: Message
    ): String { // TODO: Теперь сообщение само знает из какого оно чата


        viewModelScope.launch {
            try {
                _uiState.value.chatList.addMessageById(chatId, message)
            } catch (e: IndexOutOfBoundsException) {
                updateChats()
            }
            _uiState.update { currentState ->
                currentState.copy()
            }
            saveMessage(message)
        }


//        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle(message.author.username)
//            .setContentText(message.content)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return ""
    }

}