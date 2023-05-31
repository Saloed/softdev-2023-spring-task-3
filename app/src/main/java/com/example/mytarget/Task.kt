package com.example.mytarget

import com.google.firebase.Timestamp
import java.util.Date

data class Task(
    val userId: String,
    var taskName: String = "",
    var taskDescription: String = "",
    var date: Timestamp = Timestamp(Date()),
    var taskDone: Boolean = false,
    var taskColor: Int,
    var documentId: String = ""
){
    constructor(): this("","","",Timestamp(Date()),false,0,"")
}
