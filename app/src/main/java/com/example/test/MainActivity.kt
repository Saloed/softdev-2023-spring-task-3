package com.example.test

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.test.data.ChatElement
import com.example.test.data.ChatList
import com.example.test.data.Message
import com.example.test.data.UserPreferencesRepository
import com.example.test.ui.ChatListScreen
import com.example.test.ui.ChatScreen
import com.example.test.ui.SettingsScreen
import com.example.test.ui.theme.TestTheme



private  val SETTINGS_NAME = "settings_preference"

val android.content.Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_NAME
)// TODO: DataStore

class MainActivity : ComponentActivity() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")

//                }
//                ChatListScreen(ChatList(listOf(ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Им12312312я", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщение"),ChatElement(1,"Имя", "к", "Сообщеgdfgdfgdfние"))))
//            ChatScreen(listOf(Message("Привет","123"), Message("Hello world!","Kotlin")))
                MessengerApp(userPreferencesRepository = userPreferencesRepository)
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
        MessengerApp(userPreferencesRepository = userPreferencesRepository)
//        SettingsScreen(userPreferencesRepository = userPreferencesRepository)
    }
}