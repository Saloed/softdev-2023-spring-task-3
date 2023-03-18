package com.example.pyculator.pages

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pyculator.R
import com.example.pyculator.eval
import com.example.pyculator.viewmodels.FavoriteElement
import java.io.File


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Page1(
    memoryList: MutableList<FavoriteElement>,
    toEval: String,
    onToEvalChange: (String) -> Unit,
    filesDir: File?,
    staticResult: String?,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var result = ""

    Box(modifier = Modifier.padding(5.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy((-5).dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = toEval,
                onValueChange = onToEvalChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                    }
                ),
            )

            // That's for preview to work properly
            result = staticResult ?: eval(memoryList, filesDir, toEval, LocalContext.current)

            Text(
                result,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            )


        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onClick = {
                memoryList.add(FavoriteElement("a${memoryList.size+1}", result, toEval))
            },
        ) {
            Icon(painterResource(id = R.drawable.baseline_save_alt_24), contentDescription = null)
        }
    }
}
