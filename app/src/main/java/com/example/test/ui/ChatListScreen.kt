package com.example.test.ui

import android.annotation.SuppressLint
import android.view.RoundedCorner
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn


import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.data.ChatElement
import com.example.test.data.ChatList

import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator

import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import com.example.test.ui.theme.Green40
import com.example.test.ui.theme.PurpleGrey80


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")

@Composable
fun ChatListScreen(
    chatList: ChatList,
    onChatClick: (ChatElement) -> Unit,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }
    val ptrState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            onRefresh()
            refreshing = false
        },
    )
    Scaffold() {

//        innerPadding-> val uiState by viewModel.uiState.collectAsState()
        Box(
            modifier = Modifier
                .pullRefresh(ptrState, true)
                .fillMaxSize()

        ) {
            PullRefreshIndicator(
                refreshing,
                ptrState,
                Modifier
                    .zIndex(5.0f)
                    .align(Alignment.TopCenter)
            )
            LazyColumn(
            ) {

                items(chatList.chats) { chat ->

                    Box(
                        modifier = modifier
                            .padding(4.dp)
                            .animateItemPlacement(
                                tween(durationMillis = 250)
                            )
                    ) {
                        ChatDisplay(chat, onChatClick)
                    }

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
            .clickable(onClick = { onChatClick(chatElement) })
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

        if (chatElement.unreadCount.value != 0) {
            Box(modifier=Modifier.clip(RoundedCornerShape(16.dp))
                .background(Green40)) {
                Text(
                    text = chatElement.unreadCount.value.toString(),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp)

                )
            }
        }
    }

}


