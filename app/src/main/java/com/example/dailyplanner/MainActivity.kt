package com.example.dailyplanner

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.dailyplanner.appbotnav.Plans
import com.example.dailyplanner.appbotnav.PlansViewModel
import com.example.dailyplanner.ui.theme.DailyPlannerTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {
    private val plansViewModel by viewModels<PlansViewModel>()
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            DailyPlannerTheme {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                Plans(
                    viewModel = plansViewModel,
                    onAddNotify = notificationManager
                )

            }
        }
    }
}

