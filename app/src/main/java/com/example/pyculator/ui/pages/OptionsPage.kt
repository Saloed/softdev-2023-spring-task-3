package com.example.pyculator.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pyculator.R
import com.example.pyculator.viewmodels.SettingsViewModel
import kotlin.math.min


private fun String.makeFloat(): Float {
    val sb = StringBuilder()
    var f = true
    for (c in this) {
        if (c.isDigit()) sb.append(c)
        if (f && c == '.') {
            sb.append(c)
            f = false
        }
    }
    return sb.toString().toFloat()
}

@Composable
fun OptionsPage(
    settingsViewModel: SettingsViewModel,
    codeFontSize: Float
) {
    val themes = listOf("light", "dark")

    var themeChangeDialogShown by remember { mutableStateOf(false) }

    Column(
        //verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth()
                .clickable { themeChangeDialogShown = true }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    "Change theme",
                    style = MaterialTheme.typography.button.copy(
                        color = MaterialTheme.colors.onSurface
                    ),
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                    contentDescription = null,
                    modifier = Modifier//.align(Alignment.End)
                )
            }
        }
        Divider()
        Card(
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    "Code font size: ",
                    style =  MaterialTheme.typography.button.copy(
                        color = MaterialTheme.colors.onSurface
                    )
                )
                BasicTextField(
                    value = codeFontSize.toString(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = {
                        val new = if (it == "") 0f else it.makeFloat()
                        settingsViewModel.changeCodeFontSize(min(new,200f))
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        color = MaterialTheme.colors.onSurface
                    )
                )
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