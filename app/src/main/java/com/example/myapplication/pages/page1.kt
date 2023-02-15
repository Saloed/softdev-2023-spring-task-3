package com.example.myapplication.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.example.myapplication.R
import com.example.myapplication.viewmodels.MemoryElement
import java.io.File


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Page1(
    memoryList: MutableList<MemoryElement>,
    toEval: String,
    onToEvalChange: (String) -> Unit,

    filesDir: File?,
    staticResult: String?,
) {
    val toExec = if (filesDir != null) {
        val file = File(filesDir, "toExec.py")
        if (file.createNewFile()) {
            val writer = file.writer()
            writer.write(LocalContext.current.getString(R.string.code_example))
            writer.close()
        }
        val reader = file.reader()
        reader.readText().also { reader.close() }
    } else "Stub!"

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

            var rememberedConstants = ""
            memoryList.forEach { rememberedConstants += it.toString() }

            println(rememberedConstants)

            // That's for preview to work properly
            result = if (staticResult == null) {
                val py = Python.getInstance()
                py.getModule("main").callAttr("main", rememberedConstants + toExec, toEval).toString()
            } else staticResult

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
                memoryList.add(MemoryElement("a${memoryList.size+1}", result))
            },
        ) {
            Icon(Icons.Filled.SaveAlt, contentDescription = "")
        }
    }
}
