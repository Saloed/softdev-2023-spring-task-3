package com.example.myapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class FavoriteElement(val variableName: String, val variable: String, val variableScript: String) {
    override fun toString(): String = "$variableName = $variable\n"
}


class MemoryViewModel: ViewModel() {
    val memoryList = MutableStateFlow(mutableStateListOf<FavoriteElement>())
}