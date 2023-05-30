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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList


class TasksViewModel: ViewModel() {
//    private val _tasks: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableListOf())
private val _tasks: SnapshotStateList<Task> = mutableStateListOf()
    val tasks: MutableList<Task>
        get() = _tasks

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun removeTask(id: Long, task: Task) {
        val task = tasks.first { it.id == id }
        _tasks.remove(task)
    }


    fun taskIsSuccessful(id: Long, isMarked: Boolean) {
        val task = tasks.first { it.id == id }
        task.isComplete.value = isMarked
    }

    fun getTaskByTask(task: Task): Task? {
        val index = tasks.indexOf(task)
        return if (index != -1) tasks[index] else null
    }

    fun getTaskById(id: Long): Task? = tasks.find { it.id == id }


    fun getTaskByDate(date: LocalDate): List<Task> = tasks.filter { it.date == date }

    fun getTaskByColor(color: Color): List<Task> = tasks.filter { it.taskColor == color }

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