package com.example.dailyplanner

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dailyplanner.appbotnav.Plans
import com.example.dailyplanner.appbotnav.PlansViewModel
import com.example.dailyplanner.ui.theme.DailyPlannerTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth

class MainActivity : ComponentActivity() {
    val plansViewModel by viewModels<PlansViewModel>()
    val db = Firebase.firestore
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            DailyPlannerTheme {
                Plans(
                    plansViewModel.getPlans(),
                    onAddPlan = {plansViewModel.addPlan(it)},
                    viewModel = plansViewModel,
                    onCheckPlan = { plansViewModel.planDone(it)}
                )
            }
        }
    }
}

