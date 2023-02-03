package com.example.myapplication

import android.os.Bundle
import android.text.method.Touch.onTouchEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.chaquo.python.android.PyApplication
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.*
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PyApplication()
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    Calculator(
                        modifier = Modifier.fillMaxSize(),
                        filesDir = applicationContext.filesDir,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Calculator(modifier: Modifier = Modifier,
               staticResult: String? = null,
               filesDir: File?,
) {
    val pagerState = rememberPagerState()
    // runBlocking { launch { pagerState.scrollToPage(1) } } // gotta learn coroutines

    HorizontalPager(count = 3, state = pagerState)  { pageNumber ->

        if (pageNumber == 0) {
            Text("page0")
        }
        if (pageNumber == 1) {
            val execInput = if (filesDir != null) {
                val file = File(filesDir, "toExec.py")
                file.createNewFile()
                val reader = file.reader()
                reader.readText(). also{reader.close()}
            } else
                """""execInput""""

            var evalInput by remember { mutableStateOf("") }
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy((-5).dp)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = evalInput,
                    onValueChange = { evalInput = it },
                    placeholder = { Text("Write here") },
                )

                // That's for preview to work properly
                val result = if (staticResult != null) {
                    staticResult
                } else {
                    val py = Python.getInstance()
                    py.getModule("main").callAttr("main", execInput, evalInput).toString()
                }
                Text(
                    result,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                )
            }
        }
        if (pageNumber == 2) {
            var execInput by remember { mutableStateOf("def f(x): return x*10") }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    value = execInput,
                    onValueChange = { execInput = it },
                    placeholder = { Text("Write here") },
                )
                Button(
                    {
                        if (filesDir != null) {
                            val file = File(filesDir, "toExec.py")
                            file.createNewFile()
                            val writer = file.writer()
                            writer.write(execInput)
                            writer.close()
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomEnd),
                    content = { Text("Save") }, // TODO: change to icon
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 1080, heightDp = 1920, fontScale = 3f)
@Composable
fun CalculatorPreview() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            Calculator(
                modifier = Modifier.fillMaxSize(),
                staticResult = "result",
                filesDir = null,
            )
        }
    }
}
