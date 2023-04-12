package com.example.pyculator

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.chaquo.python.Python
import com.chaquo.python.android.PyApplication
import com.example.pyculator.ui.PyCulatorApp
import com.example.pyculator.viewmodels.MemoryViewModel


val memoryViewModel = MemoryViewModel()
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val py = Python.getInstance()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PyApplication()
            PyCulatorApp(
                memoryViewModel = memoryViewModel,
                context = applicationContext,
            )
        }
    }
}





