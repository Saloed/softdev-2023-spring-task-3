package com.example.mytarget

import androidx.lifecycle.ViewModel
import java.util.Date

class SaveData: ViewModel() {
    var textFieldHeader: String = ""
    var textFieldDescription: String = ""
    val todoList: List<Map<Pair<String,String>, Date>> = mutableListOf()
    fun resetFields() {
        textFieldHeader = ""
        textFieldDescription = ""
    }


}