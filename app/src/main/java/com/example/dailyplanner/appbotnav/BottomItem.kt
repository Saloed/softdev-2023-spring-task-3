package com.example.dailyplanner.appbotnav



import com.example.dailyplanner.R

sealed class BottomItem(val title:String,val iconID:Int,val route:String){
    object Plans:BottomItem("Plans",R.drawable.plan_icon,"screen_1")
    object Calendar:BottomItem("Calendar",R.drawable.calendar_icon,"screen_2")
    object Profile:BottomItem("Profile",R.drawable.profile_icon,"screen_3")
}
