package com.example.test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.test.data.ChatDatabase
import com.example.test.data.OfflineChatRepository
import com.example.test.data.OfflineMessageRepository
import com.example.test.data.UserPreferencesRepository
import com.example.test.ui.theme.TestTheme

val CHANNEL_ID = "Messages";
private val SETTINGS_NAME = "settings_preference"

val android.content.Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_NAME
)

class MainActivity : ComponentActivity() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        val chatDatabase by lazy {
            OfflineChatRepository(
                ChatDatabase.getDatabase(applicationContext).ChatElementDAO()
            )
        }
        val messageDatabase by lazy {
            OfflineMessageRepository(
                ChatDatabase.getDatabase(applicationContext).MessageElementDAO()
            )
        }



        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        userPreferencesRepository = UserPreferencesRepository(dataStore)
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                      Greeting("Android")

//                }
//                ChatListScreen(ChatList(listOf(ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Им12312312я", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщеgdfgdfgdfние"))))
//            ChatScreen(listOf(Message("Привет","123"), Message("Hello world!","Kotlin")))
                MessengerApp(
                    userPreferencesRepository = userPreferencesRepository,
                    chatRepository = chatDatabase,
                    messageRepository=messageDatabase
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestTheme {
//        ChatListScreen(ChatList(listOf(ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Им12312312я", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщеgdfgdfgdfние"))))
//        ChatScreen(listOf(Message("Привет","123"), Message("Hello world!","Kotlin")))
        val context = LocalContext.current
        val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
        val database by lazy {
            OfflineChatRepository(
                ChatDatabase.getDatabase(context).ChatElementDAO()
            )
        }
        val msgDatabase by lazy {
            OfflineMessageRepository(ChatDatabase.getDatabase(context).MessageElementDAO())
        }
        MessengerApp(
            userPreferencesRepository = userPreferencesRepository,
            chatRepository = database, messageRepository = msgDatabase
        )
//          SettingsScreen(userPreferencesRepository = userPreferencesRepository)
    }
}