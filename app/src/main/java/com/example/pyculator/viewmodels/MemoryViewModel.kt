package com.example.pyculator.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow


data class FavoriteVariable(val variableName: String, val variable: String, val variableScript: String) {
    override fun toString(): String = "$variableName = $variable\n"
}


class MemoryViewModel: ViewModel() {
    val memoryList = MutableStateFlow(mutableStateListOf<FavoriteVariable>())
}