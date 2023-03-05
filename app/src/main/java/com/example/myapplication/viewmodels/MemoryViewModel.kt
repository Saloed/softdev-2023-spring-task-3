package com.example.myapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class MemoryElement(var variableName: String, var variable: String, var variableScript: String) {
    override fun toString(): String = "$variableName = $variable\n"
}


class MemoryViewModel: ViewModel() {
    val memoryList = MutableStateFlow(mutableStateListOf<MemoryElement>())
    //val memoryList = _memoryList.asStateFlow()

}