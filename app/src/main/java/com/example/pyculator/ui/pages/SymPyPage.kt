package com.example.pyculator.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pyculator.R
import com.example.pyculator.utils.symPy
import com.example.pyculator.viewmodels.FavoriteVariable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val funcsForFunction = listOf(
    "integrate" to "Integral",
    "roots" to "Roots",
    "diff" to "Derivative",
    "is_decreasing" to "Decreasing",
    "is_increasing" to "Increasing",
)
val funcsForNumber = listOf(
    "primefactors" to "Prime factors",
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SymPyPage(
    coroutineScope: CoroutineScope,
    memoryList: SnapshotStateList<FavoriteVariable>,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var toSymPy by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.padding(5.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy((-5).dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = toSymPy,
                onValueChange = { toSymPy = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                    }
                ),
            )

            Text(
                result,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            )
        }



        /*
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onClick = {
                memoryList.add(FavoriteVariable("a${memoryList.size+1}", result, toSymPy))
            },
        ) {
            Icon(painterResource(id = R.drawable.baseline_save_alt_24), contentDescription = null)
        }
        */

        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onClick = {
                result = ""
                coroutineScope.launch {
                    if ( toSymPy.matches(Regex("""-?\d+""")) ) {
                        toSymPy.toBigInteger()
                        for ((func, funcName) in funcsForNumber) {
                            val res = symPy(func, toSymPy)

                            result += "$funcName: $res\n"
                            scrollState.scrollTo(scrollState.maxValue)
                        }
                    } else {
                        for ((func, funcName) in funcsForFunction) {
                            val res = symPy(func, toSymPy)
                            result += "$funcName: $res\n"
                            scrollState.scrollTo(scrollState.maxValue)
                        }
                    }
                }
            }
        ) {
            Icon(painterResource(R.drawable.baseline_play_arrow_24), contentDescription = null)
        }

    }
}