package com.example.dailyplanner
data class Plan(
    val userId: String,
    var date: String = "",
    var time: String = "",
    var planText: String = "",
    var useful_habit: Boolean,
    var planDone: Boolean = false,
    var documentId: String = ""
) {

    constructor() : this("", "", "", "", true, false, "")
}
