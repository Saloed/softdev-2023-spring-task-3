package com.example.dailyplanner

data class Plan(
    val date: String = "",
    val time_planText:Pair<String, String>,
    val useful_habit:  Boolean,
    var planDone: Boolean
)
private fun planData () = arrayListOf(
    Plan("22 May 2023", "10:00" to "sleep",true, false),
)
object DataRepo{
    fun getPlan() = planData()
}