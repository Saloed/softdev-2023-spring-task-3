package com.example.mytarget.tryingthisshit


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mytarget.StorageRepo
import com.example.mytarget.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Date
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class NewTasksViewModel(private val repository: StorageRepo = StorageRepo()):ViewModel() {
    var taskListUiState by mutableStateOf(TaskListUiState())
    var taskUiState by mutableStateOf(TaskUiState())

    private val hasUser: Boolean
        get() = repository.hasUser()

    private fun userId() = Firebase.auth.currentUser?.uid.orEmpty()

    fun loadTasks() {
        if (hasUser) {
            if (userId().isNotBlank()) {
                getUserTasks(userId())
            }
        } else {
            taskListUiState = taskListUiState.copy(
                taskList = StorageRepo.Resources.Error(
                    throwable = Throwable(message = "User is not Login")
                )
            )
        }
    }

    private fun getUserTasks(userId: String) = viewModelScope.launch {
        repository.getUserTasks(userId).collect {
            taskListUiState = taskListUiState.copy(taskList = it)
        }
    }

    fun addTask() {
        if (hasUser) {
            repository.addTask(
                userId = userId(),
                date = taskUiState.date,
                taskName = taskUiState.taskName,
                taskDescription = taskUiState.taskDescription,
                isComplete = taskUiState.isComplete,
                taskColor = taskUiState.taskColor
            )
        }
    }

    fun updateTask(
        taskId: String,

        ){
        repository.updateTask(
            taskId = taskId,
            isComplete = getTask(taskId).isComplete
        )
    }

     fun getTask(id: String): Task {
        val taskList =  taskListUiState.taskList.data!!
        return taskList.first{it.documentId == id}
    }

    fun onDateChange(date: Timestamp) {
        taskUiState = taskUiState.copy(date = date)
    }

    fun onNameChange(name: String) {
        taskUiState = taskUiState.copy(taskName = name)
    }

    fun onDescriptionChange(description: String) {
        taskUiState = taskUiState.copy(taskDescription = description)
    }

    fun onComplete(isComplete: Boolean) {
        taskUiState = taskUiState.copy(isComplete = isComplete)
    }

    fun onColorChange(color: Int) {
        taskUiState = taskUiState.copy(taskColor = color)
    }

    fun deleteTask(id: String) = repository.deleteTask(id)

    fun getCurrentDayPlans(date: Timestamp): List<Task> =
        if (taskListUiState.taskList.data != null) taskListUiState.taskList.data!!.filter { it.date == date } else listOf()


    fun getTasksForCurrentWeek(tasks: List<Task>): List<Task> {
        val currentDate = LocalDate.now()
        val nearestWeekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val nearestWeekEnd = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return tasks.filter { task ->
            val taskDate = toLocalDate(task.date)
            taskDate.isAfter(nearestWeekStart.minusDays(1)) && taskDate.isBefore(nearestWeekEnd.plusDays(1))
        }
    }

    fun getTasksForCurrentMonth(tasks: List<Task>): List<Task> {
        val currentDate = LocalDate.now()
        val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth())

        return tasks.filter { task ->
            val taskDate = toLocalDate(task.date)
            taskDate.isAfter(firstDayOfMonth.minusDays(1)) && taskDate.isBefore(lastDayOfMonth.plusDays(1))
        }
    }

    private fun toLocalDate(date: Timestamp): LocalDate {
        return date.toDate()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

}



data class TaskUiState(
    val userId: String = "",
    var taskName: String = "",
    var taskDescription: String = "",
    var date: Timestamp = Timestamp(Date()),
    var isComplete: Boolean = false,
    var taskColor: Int = 0,
    var documentId: String = ""
)

data class TaskListUiState(
    val taskList: StorageRepo.Resources<List<Task>> = StorageRepo.Resources.Loading()
)