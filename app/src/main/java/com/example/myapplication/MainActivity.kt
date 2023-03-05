package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.chaquo.python.Python
import com.chaquo.python.android.PyApplication
import com.example.myapplication.pages.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodels.FavoriteElement
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.io.File
import com.example.myapplication.viewmodels.MemoryViewModel
import kotlinx.coroutines.launch


val memoryViewModel = MemoryViewModel()
val py = Python.getInstance()


fun eval(
    memoryList: MutableList<FavoriteElement>,
    filesDir: File?,
    toEval: String,
    context: Context
): String {
    val toExec = if (filesDir != null) {
        val file = File(filesDir, "toExec.py")
        if (file.createNewFile()) {
            file.writer().use {
                it.write(context.getString(R.string.code_example))
            }
        }
        file.reader().use { it.readText() }
    } else "pass"

    var rememberedConstants = ""
    memoryList.forEach { rememberedConstants += it.toString() }

    return py.getModule("main").callAttr(
        "main",
        rememberedConstants + toExec,
        toEval
    ).toString()
}

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


@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Calculator(
    memoryViewModel: MemoryViewModel,
    staticResult: String? = null, // Preview can't calculate result
    filesDir: File?,
) {
    MyApplicationTheme {

        val keyboard = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()
        val memoryList by memoryViewModel.memoryList.collectAsState()
        var toEval by remember { mutableStateOf("") }

        val pagerState = rememberPagerState(1)
        val scaffoldState = rememberScaffoldState()
        val pageNames = listOf(
            LocalContext.current.getString(R.string.Favorites),
            LocalContext.current.getString(R.string.Expression),
            LocalContext.current.getString(R.string.Code),
            LocalContext.current.getString(R.string.Settings),
        )
        val pageIcons = listOf(
            painterResource(R.drawable.baseline_favorite_24),
            painterResource(R.drawable.baseline_create_24),
            painterResource(R.drawable.baseline_code_24),
            painterResource(R.drawable.baseline_settings_24),
        )

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect {
                keyboard?.hide()
            }
        }

        // HorizontalPager with BottomAppBar
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomNavigation {
                    for (pageNumber in pageNames.indices) {
                        BottomNavigationItem(
                            icon = {
                                Icon(pageIcons[pageNumber], null)
                            },
                            selected = pageNumber == pagerState.currentPage,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pageNumber)
                                }
                            },
                            selectedContentColor = Color.Magenta,
                            unselectedContentColor = Color.LightGray,
                            label = { Text(pageNames[pageNumber]) }
                        )
                    }
                }
            }
        ) {
            HorizontalPager(
                contentPadding = it,
                count = 4,
                state = pagerState,
            ) { pageNumber ->
                when (pageNumber) {
                    0 -> Page0(
                        memoryList = memoryList,
                        onPaste = { toEval += it },
                        filesDir = filesDir,
                    )
                    1 -> Page1(
                        memoryList = memoryList,
                        toEval = toEval,
                        onToEvalChange = { toEval = it },
                        filesDir = filesDir,
                        staticResult = staticResult,
                    )
                    2 -> Page2(
                        filesDir
                    )
                    3 -> Page3()
                }
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
