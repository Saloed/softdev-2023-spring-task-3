//package com.example.dailyplanner.appbotnav
//
//import android.annotation.SuppressLint
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.rememberNavController
//
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MainScreen() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = { AppBottomNavigation(navController = navController) }
//    )
//    {
//    NavGraph(navHostController = navController)
//    }
//}