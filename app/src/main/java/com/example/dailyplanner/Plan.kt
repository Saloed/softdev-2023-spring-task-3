package com.example.dailyplanner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Plan(
    public val userId: String,
    public var date: String = "",
    public var time: String = "",
    public var planText: String = "",
    public var useful_habit: Boolean,
    public var planDone: Boolean = false,
    public var documentId: String = ""
) {

   constructor() : this("","","","",true, false,"",)
}
