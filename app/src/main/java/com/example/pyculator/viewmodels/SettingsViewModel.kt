package com.example.pyculator.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pyculator.dataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsState(
    theme: String,
    codeFontSize: Int
)

private data class SettingsViewModelState(
    val theme: String,
    val codeFontSize: Int
) {
    fun toUiState(): SettingsState = SettingsState(
        theme = theme,
        codeFontSize = codeFontSize
    )

}

@SuppressLint("StaticFieldLeak")
class SettingsViewModel (
    private val context: Context,
    // "This field leaks a context object" warning is kinda false
) : ViewModel() {
    val themeFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[stringPreferencesKey("theme")] ?: "light" }
    val codeFontSizeFlow: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[intPreferencesKey("codeFontSize")] ?: 16 }


    fun changeTheme(new: String) {
        viewModelScope.launch { context.edit(stringPreferencesKey("theme"), new) }
    }
    fun changeCodeFontSize(new: Int) {
        viewModelScope.launch { context.edit(intPreferencesKey("codeFontSize"), new) }
    }
}

private suspend fun <T> Context.edit(key: Preferences.Key<T>, value: T) {
    this.dataStore.edit { settings ->
        settings[key] = value
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