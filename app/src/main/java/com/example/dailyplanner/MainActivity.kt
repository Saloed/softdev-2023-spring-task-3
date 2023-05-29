package com.example.dailyplanner

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.example.dailyplanner.appbotnav.Plans
import com.example.dailyplanner.appbotnav.PlansViewModel
import com.example.dailyplanner.ui.theme.DailyPlannerTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth

class MainActivity : ComponentActivity() {
    private val plansViewModel by viewModels<PlansViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            DailyPlannerTheme {
                Plans(
                    plansViewModel.planListUiState.planList.data,
                    onAddPlan = {plansViewModel.addPlan()},
                    viewModel = plansViewModel,
                    onCheckPlan = { plansViewModel.onPlanDoneChange(plansViewModel.planUiState.planDone)}
                )
            }
        }
    }
}

