package com.example.test


import android.widget.Toast
import androidx.annotation.StringRes


import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import com.example.test.ui.ChatListScreen
import com.example.test.ui.ChatScreen

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.res.stringResource
import com.example.test.data.ChatRepository
import com.example.test.data.OfflineMessageRepository

import com.example.test.data.UserPreferencesRepository
import com.example.test.ui.SettingsScreen


enum class MessengerRoutes(@StringRes val title: Int) {
    ChatList(R.string.app_name),
    Chat(R.string.chat),
    Settings(R.string.settings)
}


@Composable
fun MessengerApp(
    modifier: Modifier = Modifier,
    userPreferencesRepository: UserPreferencesRepository,
    chatRepository: ChatRepository,
    messageRepository: OfflineMessageRepository
) {
    val navController = rememberNavController()
    val viewModel = ChatViewModel(userPreferencesRepository, chatRepository, messageRepository)
    try {
        viewModel.loadChats()
    } catch (e: Exception) {
        Toast.makeText(LocalContext.current, R.string.db_loading_error, Toast.LENGTH_LONG).show()
    }

    viewModel.telegramInit(LocalContext.current.filesDir.absolutePath)

    var topBarText by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(
                onSettingsButtonClicked = { navController.navigate(MessengerRoutes.Settings.name) },
                onNavigateUpButtonClicked = { navController.navigateUp() },
                onRefreshButtonClicked = { viewModel.updateChats() },
                text = topBarText
            )
        }
    )
    { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = MessengerRoutes.ChatList.name,
            modifier = modifier.padding(innerPadding)
        ) {

            composable(route = MessengerRoutes.ChatList.name) {
                val context = LocalContext.current
                topBarText = stringResource(R.string.app_name)

                EnterAnimation {
                    ChatListScreen(uiState.chatList, onChatClick = {
                        viewModel.setSelectedChat(it)
                        navController.navigate(MessengerRoutes.Chat.name)
                    }, onRefresh = { viewModel.updateChats() }
                    )

                }
            }
            composable(route = MessengerRoutes.Chat.name) {
                if (uiState.selectedChat != null) {
                    topBarText = uiState.selectedChat!!.name
                    ChatScreen(
                        uiState.selectedChat!!,
                        userPreferencesRepository = userPreferencesRepository,
                        sendAction = { viewModel.sendMessage(it) }
                    )
                }

            }
            composable(route = MessengerRoutes.Settings.name) {
                topBarText = stringResource(R.string.settings)
//                    userPreferencesRepository.saveFontSizePreference(13)

                SettingsScreen(
                    userPreferencesRepository = userPreferencesRepository,
                    viewModel = viewModel
                )
            }
        }
    }
}


@Composable
fun EnterAnimation(content: @Composable () -> Unit) {

    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(initialAlpha = 0.9f) + slideInHorizontally(),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        content()
    }
}

@Composable()
fun TopBar(
    modifier: Modifier = Modifier,
    onSettingsButtonClicked: () -> Unit,
    text: String = stringResource(R.string.app_name),
    onNavigateUpButtonClicked: () -> Unit,
    onRefreshButtonClicked: () -> Unit
) {
    val canNavigateBack = true
    TopAppBar(
        title = { Text(text = text) },
        modifier = modifier,
        navigationIcon = {

            if (canNavigateBack) {
                IconButton(onClick = onNavigateUpButtonClicked) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = text
                    )
                }
            }
            IconButton(
                onClick = onSettingsButtonClicked
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = text
                )
            }
            IconButton(
                onClick = onRefreshButtonClicked
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = text
                )
            }
        }
    )
}
