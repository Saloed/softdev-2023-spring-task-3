package com.example.dailyplanner.appbotnav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.util.Calendar

@Composable
fun Plans() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
        text = "Here ur plan",
        textAlign = TextAlign.Center
    )
}
@Composable
fun Calendar() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
        text = "Here ur calendar",
        textAlign = TextAlign.Center
    )
}
@Composable
fun Profile() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
        text = "Here ur profile",
        textAlign = TextAlign.Center
    )
}