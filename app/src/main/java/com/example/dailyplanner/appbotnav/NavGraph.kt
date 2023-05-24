//package com.example.dailyplanner.appbotnav
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.Composable
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun NavGraph(navHostController: NavHostController) {
//    NavHost(
//        navController = rememberNavController(),
//        startDestination = "screen_1"
//    ) {
//        composable("screen_1"){ Plans(viewModel = viewModel())}
//// composable("screen_3"){ Profile()}
//}
//
//}