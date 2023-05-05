package com.example.test.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {


    private companion object {
        val FONT_SIZE = intPreferencesKey("font_size")
        val DISCORD_TOKEN = stringPreferencesKey("discord_token")
        const val TAG = "UserPreferencesRepo"
    }

    val fontSize: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[FONT_SIZE] ?: 10
        }

    val discordToken:Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[DISCORD_TOKEN] ?: "10"
        }

    suspend fun saveFontSizePreference(fontSize: Int) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE] = fontSize
        }
    }
    suspend fun saveDiscordTokenPreference(discordToken: String) {
        dataStore.edit { preferences ->
            preferences[DISCORD_TOKEN] = discordToken
        }
    }

}
