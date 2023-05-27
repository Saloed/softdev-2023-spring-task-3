package com.example.mytarget

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Date


class TasksViewModel: ViewModel() {
    private var _tasks: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableListOf())

    val tasks: MutableList<Task>
        get() = _tasks.value

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun removeTask(task: Task) {
        tasks.remove(task)
    }

//    fun markTaskComplete(task: Task) {
//        task.isComplete = true
//    }
//    fun markTaskIncomplete(task: Task) {
//        task.isComplete = false
//    }
//    fun markTask(mark: Boolean,task: Task){
//        task.isComplete = mark
//    }
    fun taskIsSuccesful(id: Long, isMarked: Boolean) {
        _tasks.update { tasks ->
            val updTask = tasks.first { it.id == id }
            updTask.isComplete.value = isMarked

            tasks
        }
    }

    fun getTaskByTask(task: Task) = tasks[tasks.indexOf(task)]

    fun getTaskById(id: Long): Task? = tasks.find { it.id == id }


    fun getTaskByDate(date: LocalDate): List<Task> = tasks.filter { it.date == date }

    fun getTaskByColor(color: Color): List<Task> = tasks.filter { it.taskColor == color }

    fun clearTasks() {
        tasks.clear()
    }

    fun sortTasksByNearestWeekStart() {
        val currentDate = LocalDate.now()
        val nearestWeekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sortedTasks = tasks.sortedBy { task ->
            val daysUntilStartOfWeek = ChronoUnit.DAYS.between(task.date, nearestWeekStart)
            if (daysUntilStartOfWeek < 0) Long.MAX_VALUE else daysUntilStartOfWeek
        }
        tasks.clear()
        tasks.addAll(sortedTasks)
    }

    fun sortTasksByNearestMonthStart() {
        val currentDate = LocalDate.now()
        val nearestMonthStart = currentDate.with(TemporalAdjusters.firstDayOfMonth())
        val sortedTasks = tasks.sortedBy { task ->
            val daysUntilStartOfMonth = ChronoUnit.DAYS.between(task.date, nearestMonthStart)
            if (daysUntilStartOfMonth < 0) Long.MAX_VALUE else daysUntilStartOfMonth
        }
        tasks.clear()
        tasks.addAll(sortedTasks)
    }
}