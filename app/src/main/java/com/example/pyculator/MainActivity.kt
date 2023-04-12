package com.example.pyculator

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.chaquo.python.Python
import com.chaquo.python.android.PyApplication
import com.example.pyculator.pages.Page0
import com.example.pyculator.pages.Page1
import com.example.pyculator.pages.Page2
import com.example.pyculator.pages.Page3
import com.example.pyculator.ui.theme.PyculatorTheme
import com.example.pyculator.viewmodels.FavoriteVariable
import com.example.pyculator.viewmodels.MemoryViewModel
import com.example.pyculator.viewmodels.SettingsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.io.File


val memoryViewModel = MemoryViewModel()
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val py = Python.getInstance()


fun eval(
    memoryList: MutableList<FavoriteVariable>,
    filesDir: File?,
    toEval: String,
    context: Context
): String {
    println("evaluating")
    /*val toExec = if (filesDir != null) {
        val file = File(filesDir, "toExec.py")
        if (file.createNewFile()) {
            file.writer().use {
                it.write(context.getString(R.string.code_example))
            }
        }
        file.reader().use { it.readText() }
    } else "pass"*/

    var rememberedConstants = ""
    memoryList.forEach { rememberedConstants += it.toString() }

    return py.getModule("main").callAttr(
        "main",
        rememberedConstants,
        toEval,
    ).toString()
}

fun compile(
    toCompile: String
): String {
    return py.getModule("main").callAttr(
        "compileCode",
        toCompile
    ).toString()
}

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


@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PyCulatorApp(
    memoryViewModel: MemoryViewModel,
    staticResult: String? = null, // Preview can't calculate result
    context: Context,
) {
    val settingsViewModel = SettingsViewModel(context)
    val settings = settingsViewModel.settingsState.collectAsState(if (isSystemInDarkTheme()) "dark" else "light")

    val filesDir = context.filesDir

    val keyboard = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
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

    // Hide keyboard on page change
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            keyboard?.hide()
        }
    }

    PyculatorTheme(settings.value) {
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
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pageNumber)
                                }
                            },
                            label = { Text(pageNames[pageNumber]) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            HorizontalPager(
                contentPadding = innerPadding,
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
                        coroutineScope = coroutineScope,
                    )
                    2 -> Page2(
                        toCompile = { toCompile: String -> compile(toCompile) },
                        context = context,
                        filesDir = filesDir
                    )
                    3 -> Page3(
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}


