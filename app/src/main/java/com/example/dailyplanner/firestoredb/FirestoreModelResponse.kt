package com.example.dailyplanner.firestoredb

import com.example.dailyplanner.Plan

data class FirestoreModelResponse(
    val item:FirestoreItem?,
    val key:String?
){

    data class FirestoreItem(
        val id:String? = "",
        val date:String? = "",
        val time:String? = "",
        val planText:String? = "",
        val planDone:Boolean = false,
        val habit: Boolean = true
    )

}