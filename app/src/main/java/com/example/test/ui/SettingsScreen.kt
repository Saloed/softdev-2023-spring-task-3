package com.example.test.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField

import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.test.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.test.ChatViewModel
import com.example.test.data.UserPreferencesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    userPreferencesRepository: UserPreferencesRepository,
    viewModel: ChatViewModel
) {

    var telegramLoginForm by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Row(modifier = Modifier.padding(4.dp)) {
            Text(
                stringResource(R.string.font_size),
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterVertically), color = MaterialTheme.colorScheme.onSurface
            )
            TextField(
                value = userPreferencesRepository.fontSize.collectAsState(16).value.toString(),
                onValueChange = {
                    runBlocking {
                        userPreferencesRepository.saveFontSizePreference(if (it != "") it.filter { it.isDigit() }
                            .toInt() else 16)
                    }
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row {
            Text(
                stringResource(R.string.discord_id),
                modifier = Modifier
                    .padding(4.dp), color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                userPreferencesRepository.discordId.collectAsState(initial = "").value,
                modifier = Modifier
                    .padding(4.dp), color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(modifier = Modifier.padding(4.dp)) {
            Text(
                stringResource(R.string.discord_token),
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterVertically), color = MaterialTheme.colorScheme.onSurface
            )
            TextField(
                value = userPreferencesRepository.discordToken.collectAsState(initial = "").value,
                onValueChange = {
                    viewModel.viewModelScope.launch {
                        userPreferencesRepository.saveDiscordTokenPreference(
                            it
                        )
                        viewModel.getMeDiscord()
                    }
                })
        }
        Text(
            userPreferencesRepository.telegramId.collectAsState(initial = "").value,
            modifier = Modifier
                .padding(4.dp), color = MaterialTheme.colorScheme.onSurface
        )
        Row(modifier = Modifier.padding(4.dp)) {
            TextField(
                value = telegramLoginForm,
                onValueChange = { telegramLoginForm = it })
        }
        Row(modifier = Modifier.padding(4.dp)) {
            Button(
                onClick = { viewModel.onTelegramPromptUpdate(telegramLoginForm) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            )

            {
                Text(
                    stringResource(R.string.submit),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsPreview() {
//    SettingsScreen()
}

