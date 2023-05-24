package com.example.dailyplanner.appbotnav

import com.example.dailyplanner.Plan

val planList = arrayListOf(
Plan("22 May 2023", "10:00" to "sleep",true, false),
)
object PlanData {
    fun getPlan() = planList
    fun getCurrentPlan(plan: Plan) = planList[planList.indexOf(plan)]
    fun getCurrentDayPlans(date: String): List<Plan> = getPlan().filter{ it.date == date }
    fun addPlan(plan:Plan) = planList.add(plan)
    fun getDone(plan: Plan) = getCurrentPlan(plan).planDone
}