package com.example.pyculator.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pyculator.utils.symPy
import kotlinx.coroutines.*

private val allFuncs = listOf(
    "integrate" to "Integral",
    "roots" to "Roots",
    "diff" to "Derivative",
    "is_decreasing" to "Decreasing",
    "is_increasing" to "Increasing",
    "primefactors" to "Prime factors",
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SymPyPage(
    coroutineScope: CoroutineScope,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var toSymPy by remember { mutableStateOf("") }
    var simplifiedToSymPy by remember { mutableStateOf("") }
    var simplifierJob by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy((-5).dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = toSymPy,
                onValueChange = {
                    toSymPy = it

                    simplifierJob?.cancel()
                    simplifierJob = coroutineScope.launch(Dispatchers.Default) {
                        simplifiedToSymPy = symPy("simplify", toSymPy)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                    }
                ),
            )

            Column(modifier = Modifier
                .verticalScroll(rememberScrollState())
            ) {
                for ((func, funcName) in allFuncs) {
                    var text by remember { mutableStateOf("") }
                    Row {
                        Button(
                            onClick = {
                                coroutineScope.launch(Dispatchers.Default) {
                                    text = symPy(func, simplifiedToSymPy)
                                }
                            }
                        ) {
                            Text(funcName)
                        }
                        Text(text)
                    }
                }
            }
        }
    }
}