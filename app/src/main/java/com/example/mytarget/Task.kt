package com.example.mytarget

import android.app.ActivityManager.TaskDescription
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class Task(
    var id: Long = 0,
    var taskName: String = "",
    var taskDescription: String = "",
    var date: LocalDate = LocalDate.now(),
    var isComplete: MutableState<Boolean> = mutableStateOf(false),
    var taskColor: Color
)
