//package com.example.dailyplanner.appbotnav
//
//import androidx.compose.runtime.mutableStateOf
//import com.example.dailyplanner.Plan
//
//val planList = arrayListOf(
//Plan("1","22 May 2023", "10:00" , "sleep",true, mutableStateOf(false)),
//)
//object PlanData {
//
//    private fun getPlan() = planList
//    private fun getCurrentPlan(plan: Plan) = planList[planList.indexOf(plan)]
//    fun getCurrentDayPlans(date: String): List<Plan> = getPlan().filter{ it.date == date }
//    fun addPlan(plan:Plan) = planList.add(plan)
//    fun getDone(plan: Plan) = getCurrentPlan(plan).planDone
//}