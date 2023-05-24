package com.example.dailyplanner.appbotnav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyplanner.Plan
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class PlansViewModel : ViewModel() {
    var planList = MutableStateFlow(
        mutableListOf(
            Plan("24 May 2023", "10:00" to "sleep", true, false),
        )
    )

    fun getPlans(): List<Plan> = planList.value


    fun addPlan(plan: Plan) {
        planList.value.add(plan)
    }

    fun planDone(plan: Plan) {
        planList.value[planList.value.indexOf(plan)].planDone =
            !planList.value[planList.value.indexOf(plan)].planDone

    }

    fun getCurrentPlan(plan: Plan) = planList.value[planList.value.indexOf(plan)]
    fun getCurrentDayPlans(date: String): List<Plan> = getPlans().filter { it.date == date }
    fun daysCheckedPlans(day: String): Float =
        (getCurrentDayPlans(day).filter { it.planDone }.size.toFloat() / getCurrentDayPlans(day).size.toFloat())

    fun HabbitCheckedPlans(day: String): Int =
        (getCurrentDayPlans(day).filter { it.planDone && it.useful_habit }.size)

}
