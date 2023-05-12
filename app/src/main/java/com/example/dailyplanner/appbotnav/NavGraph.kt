package com.example.dailyplanner.appbotnav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = "screen_1"
    ) {
        composable("screen_1"){ Plans()}
        composable("screen_2"){ Calendar()}
        composable("screen_3"){ Profile()}
    }

}