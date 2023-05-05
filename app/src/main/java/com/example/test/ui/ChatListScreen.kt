package com.example.test.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn


import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.data.ChatElement
import com.example.test.data.ChatList

import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.ui.draw.clip
import com.example.test.R


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")

@Composable
fun ChatListScreen(
    chatList: ChatList,
    onChatClick: (ChatElement) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold() {
//        innerPadding-> val uiState by viewModel.uiState.collectAsState()
        LazyColumn {

            items(chatList.chats) { chat ->
                Box(modifier = modifier.padding(4.dp).animateItemPlacement(tween(durationMillis = 250) // TODO: Тут анимация
                )) {
                    ChatDisplay(chat, onChatClick)
                }
            }

        }
    }

}

@Composable
fun ChatDisplay(
    chatElement: ChatElement,
    onChatClick: (ChatElement) -> Unit = {},
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(com.example.test.ui.theme.Purple80)
            .fillMaxWidth()
            .clickable(onClick =  { onChatClick(chatElement)  })
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)

        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = chatElement.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = chatElement.displayMessage,
                fontSize = 16.sp
            )


        }
    }
}


