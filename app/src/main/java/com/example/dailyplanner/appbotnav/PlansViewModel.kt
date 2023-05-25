package com.example.dailyplanner.appbotnav

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dailyplanner.Plan
import kotlinx.coroutines.flow.MutableStateFlow

class PlansViewModel : ViewModel() {
    private var _planList = MutableStateFlow(
        mutableListOf(
            Plan("0", "25 May 2023", "10:00" to "sleep", true, mutableStateOf(false) ),
        )
    )
    val planList: MutableList<Plan>
        get() = _planList.value


    fun addPlan(plan: Plan) {
        planList.add(plan)
    }

    fun planIsDone(id: String, isChecked: Boolean) {
        val updatedPlan = getPlanById(id)?.copy(planDone = mutableStateOf(isChecked))
        val forUpdate = mutableListOf<Plan>()
        planList.remove( getPlanById(id))
        forUpdate.addAll(planList)
        forUpdate.add(updatedPlan!!)
        forUpdate.sortBy { it.id }
        _planList= MutableStateFlow(forUpdate)

    }

    fun getPlanById(id: String): Plan? = planList.find { it.id == id }


    fun getCurrentPlan(plan: Plan) = planList[planList.indexOf(plan)]
    fun getCurrentDayPlans(date: String): List<Plan> = planList.filter { it.date == date }
    fun daysCheckedPlans(day: String): Float =
        if (getCurrentDayPlans(day).size != 0) (getCurrentDayPlans(day).filter { it.planDone.value }.size.toFloat() / getCurrentDayPlans(
            day
        ).size.toFloat()) else 0f

    fun habbitCheckedPlans(day: String): Int =
        (getCurrentDayPlans(day).filter { it.planDone.value && it.useful_habit }.size)

}
