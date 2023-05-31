package com.example.mytarget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.mytarget.tryingthisshit.NewTasksViewModel
import com.example.mytarget.ui.theme.MyTargetTheme

class MainActivity : ComponentActivity() {
    private val tasksViewModel by viewModels<NewTasksViewModel>()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTargetTheme {
                MyTargetApp(
                    viewModel = tasksViewModel,
                    onAddTask = {tasksViewModel.addTask()},
                    onComplete = {tasksViewModel.onComplete(tasksViewModel.taskUiState.isComplete)}
                )

            }
        }
    }
}
