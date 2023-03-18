package com.example.pyculator.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pyculator.dataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class SettingsViewModel (
    private val context: Context,
    // "This field leaks a context object" warning is kinda false
) : ViewModel() {
    private val themeFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[stringPreferencesKey("theme")] ?: "light" }
    val settingsState = themeFlow


    fun changeTheme(new: String) {
        viewModelScope.launch { context.edit("theme", new) }
    }
}

private suspend fun Context.edit(key: String, value: String) {
    val actualKey = stringPreferencesKey(key)
    this.dataStore.edit { settings ->
        settings[actualKey] = value
    }
}

private suspend fun Context.get(key: String, defaultValue: String): String {
    val actualKey = stringPreferencesKey(key)
    var new = ""
    this.dataStore.data.last()
    this.dataStore.data.collect {value ->
        println("1 " + value[actualKey])
        new = value[actualKey] ?: defaultValue
    }
    return new
}