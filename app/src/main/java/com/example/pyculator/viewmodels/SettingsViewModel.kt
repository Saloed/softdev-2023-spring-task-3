package com.example.pyculator.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pyculator.dataStore
import com.example.pyculator.ui.theme.Theme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/* It will be better to make it all like that but what I did is enough
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

} */

@SuppressLint("StaticFieldLeak")
class SettingsViewModel (
    private val context: Context,
    // "This field leaks a context object" warning is kinda false
    private val defaultFontSize: Float,
) : ViewModel() {
    val themeFlow: Flow<Theme> = context.dataStore.data
        .map { preferences ->
            when (preferences[stringPreferencesKey("theme")] ?: "light") {
                "light" -> Theme.LIGHT
                "dark" -> Theme.DARK
                else -> Theme.UNDEFINED
            }
        }
    val codeFontSizeFlow: Flow<Float> = context.dataStore.data
        .map { preferences ->
            preferences[floatPreferencesKey("codeFontSize")] ?: defaultFontSize
        }

    fun changeTheme(new: String) {
        viewModelScope.launch { context.edit(stringPreferencesKey("theme"), new) }
    }
    fun changeCodeFontSize(new: Float) {
        viewModelScope.launch { context.edit(floatPreferencesKey("codeFontSize"), new) }
    }
}

private suspend fun <T> Context.edit(key: Preferences.Key<T>, value: T) {
    this.dataStore.edit { settings ->
        settings[key] = value
    }
}