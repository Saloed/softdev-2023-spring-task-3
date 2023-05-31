package com.example.mytarget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mytarget.ui.theme.MyTargetTheme

class MainActivity : ComponentActivity() {
    private val tasksViewModel by viewModels<NewTasksViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTargetTheme {
                MyTargetApp(
                    viewModel = tasksViewModel
                )
            }
        }
    }
}
