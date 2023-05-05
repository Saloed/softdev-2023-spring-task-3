package com.example.test.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.data.UserPreferencesRepository
import kotlinx.coroutines.runBlocking


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    userPreferencesRepository: UserPreferencesRepository
) {
    Column(modifier = modifier) {

        Row(modifier=Modifier.padding(4.dp)) {
            Text(stringResource(R.string.font_size), modifier = Modifier.padding(4.dp).align(Alignment.CenterVertically))
            TextField(
                value = userPreferencesRepository.fontSize.collectAsState(16).value.toString(),
                onValueChange = {
                    runBlocking {
                        userPreferencesRepository.saveFontSizePreference(if (it != "") it.filter { it.isDigit() }
                            .toInt() else 16)
                    } // TODO: хз нормально ли
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(modifier=Modifier.padding(4.dp)) {
            Text(stringResource(R.string.discord_token), modifier = Modifier.padding(4.dp).align(Alignment.CenterVertically))
            TextField(
                value = userPreferencesRepository.discordToken.collectAsState(initial = "").value,
                onValueChange = { runBlocking{userPreferencesRepository.saveDiscordTokenPreference(it)}})
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsPreview() {
//    SettingsScreen()
}