package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.chaquo.python.android.PyApplication
import com.example.myapplication.pages.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.io.File
import com.example.myapplication.viewmodels.MemoryViewModel


val memoryViewModel = MemoryViewModel()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PyApplication()
            Calculator(
                memoryViewModel,
                filesDir = applicationContext.filesDir,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Calculator(
    memoryViewModel: MemoryViewModel,
    staticResult: String? = null, // Preview can't calculate result
    filesDir: File?,
) {
    MyApplicationTheme {

        val memoryList by memoryViewModel.memoryList.collectAsState()

        val pagerState = rememberPagerState(1)
        var toEval by remember { mutableStateOf("") }

        HorizontalPager(count = 3, state = pagerState) { pageNumber ->
            if (pageNumber == 0) {
                Page0(
                    memoryList = memoryList,
                    onPaste = { toEval += it },
                )
            }
            if (pageNumber == 1) {
                Page1(
                    memoryList = memoryList,
                    toEval = toEval,
                    onToEvalChange = { toEval = it },
                    filesDir = filesDir,
                    staticResult = staticResult
                )
            }
            if (pageNumber == 2) {
                Page2(filesDir)
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640, fontScale = 1f)
@Composable
fun CalculatorPreview() {
    Calculator(
        memoryViewModel,
        staticResult = "Result",
        filesDir = null,
    )
}
