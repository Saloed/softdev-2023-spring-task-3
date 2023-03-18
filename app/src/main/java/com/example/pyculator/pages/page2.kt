package com.example.pyculator.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pyculator.R

import java.io.File


@Composable
fun Page2(
    filesDir: File?,
) {
    var execInput by remember {
        mutableStateOf(
            if (filesDir != null) {
                val file = File(filesDir, "toExec.py")
                val reader = file.reader()
                reader.readText().also { reader.close() }
            } else {
                "execInput will be here"
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize()
                //.horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState()),

            value = execInput,
            onValueChange = { execInput = it },
            //textStyle = TextStyle(fontFamily = FontFamily.Monospace),
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(60.dp),
            onClick = {
                if (filesDir != null) {
                    val file = File(filesDir, "toExec.py")
                    file.createNewFile()
                    val writer = file.writer()
                    writer.write(execInput)
                    writer.close()
                }
            },
        ) {
            Icon(painterResource(R.drawable.baseline_save_24), null)
        }
    }
}