package com.example.test.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.data.ChatElement
import com.example.test.data.Message
import com.example.test.data.UserPreferencesRepository

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    chat: ChatElement,
    modifier: Modifier = Modifier,
    userPreferencesRepository: UserPreferencesRepository
) {
    Scaffold() {
//        innerPadding-> val uiState by viewModel.uiState.collectAsState()
        Button(onClick = { chat.updatemessages() },modifier=Modifier.absolutePadding(left=150.dp)) {
            Text("Обновить")
        }
        Spacer(modifier = Modifier.padding(16.dp))
        LazyColumn(reverseLayout = true) {

            items(chat.messages) { message ->
                Box(modifier = modifier.padding(4.dp)) {
                    val fontSize =
                        userPreferencesRepository.fontSize.collectAsState(initial = 16).value
                    messageDisplay(message, contentFontSize = fontSize)
                }
            }

        }
    }
}

@Composable
fun messageDisplay(message: Message, modifier: Modifier = Modifier, contentFontSize: Int) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(com.example.test.ui.theme.Purple80)
    ) {

        Column(modifier = modifier.padding(horizontal = 8.dp)) {
            Text(
                text = message.content,
                fontSize = contentFontSize.sp
            )
            Text(
                text = message.sender,
                fontWeight = FontWeight.Thin
            )
        }

    }
}