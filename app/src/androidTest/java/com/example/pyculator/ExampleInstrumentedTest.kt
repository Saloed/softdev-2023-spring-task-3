package com.example.pyculator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pyculator.utils.eval
import com.example.pyculator.viewmodels.FavoriteVariable
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pyculator", appContext.packageName)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals("4", eval(
            mutableListOf(FavoriteVariable(
                variableName = "a",
                variable = "2",
                variableScript = "2"
            )),
            "a+2"))
    }
}