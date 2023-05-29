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

    fun removeTask(id: Long, task: Task) {
        _tasks.update { tasks ->
            val updTask = tasks.first {it.id == id}
            tasks.remove(updTask)
            tasks
        }
    }

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

    fun getTasksForCurrentWeek(tasks: List<Task>): List<Task> {
        val currentDate = LocalDate.now()
        val nearestWeekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val nearestWeekEnd = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return tasks.filter { task ->
            task.date.isAfter(nearestWeekStart.minusDays(1)) && task.date.isBefore(nearestWeekEnd.plusDays(1))
        }
    }

    fun getTasksForCurrentMonth(tasks: List<Task>): List<Task> {
        val currentDate = LocalDate.now()
        val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth())

        return tasks.filter { task ->
            task.date.isAfter(firstDayOfMonth.minusDays(1)) && task.date.isBefore(lastDayOfMonth.plusDays(1))
        }
    }
}