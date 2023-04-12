package com.example.pyculator.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pyculator.R
import com.example.pyculator.viewmodels.SettingsViewModel


@Composable
fun Page3(
    settingsViewModel: SettingsViewModel
) {
    val themes = listOf("light", "dark") // TODO

    var themeChangeDialogShown by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            TextButton(
                modifier = Modifier

                    .fillMaxSize(),
                onClick = { themeChangeDialogShown = true }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Change theme")
                    Icon(
                        painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
            }
        }
    }

    if (themeChangeDialogShown) {
        ThemeDialog(
            { themeChangeDialogShown = false },
            themes,
            settingsViewModel
        )
    }
}

@Composable
private fun ThemeDialog(
    onDismissRequest: () -> Unit,
    themes: List<String>,
    settingsViewModel: SettingsViewModel
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (theme in themes) {
                Button(
                    onClick = {
                        settingsViewModel.changeTheme(theme)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(theme)
                }
            }
        }
    }
}