package com.example.dailyplanner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Plan(
    val id: String = "",
    val date: String = "",
    val time_planText:Pair<String, String>,
    val useful_habit:  Boolean,
    var planDone: MutableState<Boolean> = mutableStateOf(false)
)
