//package com.example.dailyplanner.appbotnav
//
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.BottomNavigationItem
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.compose.currentBackStackEntryAsState
//
//@Composable
//fun AppBottomNavigation(navController:NavController){
//    val listItems=listOf(
//        BottomItem.Plans,
//        BottomItem.Profile
//    )
//    androidx.compose.material.BottomNavigation(backgroundColor=Color.White, modifier = Modifier.height(55.dp)){
//        val backStackEntry by navController.currentBackStackEntryAsState()
//        val currentRout=backStackEntry?.destination?.route
//        listItems.forEach{item->
//            BottomNavigationItem(
//                selected=currentRout==item.route,
//                onClick={navController.navigate(item.route)},
//                icon={
//                    Icon(painter=painterResource(id=item.iconID),contentDescription="Icon")
//                },
//                label={
//                    Text(text=item.title,fontSize=9.sp)
//                },
//                selectedContentColor=Color.Green,
//                unselectedContentColor=Color.Gray)
//        }
//    }
//}