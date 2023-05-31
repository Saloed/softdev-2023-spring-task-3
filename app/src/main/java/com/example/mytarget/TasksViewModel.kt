//package com.example.mytarget
//
//import androidx.lifecycle.ViewModel
//import java.time.DayOfWeek
//import java.time.LocalDate
//import java.time.temporal.TemporalAdjusters
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.snapshots.SnapshotStateList
//
//
//class TasksViewModel: ViewModel() {
//    private val _tasks: SnapshotStateList<Task> = mutableStateListOf()
//    val tasks: MutableList<Task>
//        get() = _tasks
//
//    fun addTask(task: Task) {
//        tasks.add(task)
//    }
//
//    fun removeTask(id: Long) {
//        val taskForRemove = tasks.first { it.id == id }
//        _tasks.remove(taskForRemove)
//    }
//
////    fun taskIsSuccessful(id: Long, isMarked: Boolean) {
////        val task = tasks.first { it.id == id }
////        task.isComplete.value = isMarked
////    }
//
//    fun getTaskByTask(task: Task): Task? {
//        val index = tasks.indexOf(task)
//        return if (index != -1) tasks[index] else null
//    }}
////
////    fun getTaskById(id: Long): Task? = tasks.find { it.id == id }
//
//
////    fun getTaskByDate(date: LocalDate): List<Task> = tasks.filter { it.date == date }
////
////    fun getTasksForCurrentWeek(tasks: List<Task>): List<Task> {
////        val currentDate = LocalDate.now()
////        val nearestWeekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
////        val nearestWeekEnd = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
////
////        return tasks.filter { task ->
////            task.date.isAfter(nearestWeekStart.minusDays(1)) && task.date.isBefore(nearestWeekEnd.plusDays(1))
////        }
////    }
////
////    fun getTasksForCurrentMonth(tasks: List<Task>): List<Task> {
////        val currentDate = LocalDate.now()
////        val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())
////        val lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth())
////
////        return tasks.filter { task ->
////            task.date.isAfter(firstDayOfMonth.minusDays(1)) && task.date.isBefore(lastDayOfMonth.plusDays(1))
////        }
////    }
////}