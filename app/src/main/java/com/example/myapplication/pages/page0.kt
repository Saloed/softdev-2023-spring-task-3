package com.example.myapplication.pages

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.example.myapplication.eval
import com.example.myapplication.viewmodels.MemoryElement
import java.io.File

@Composable
private fun MemoryElementCard(
    variable: String,
    onVariableChange: (String) -> Unit,
    variableName: String,
    onVariableNameChange: (String) -> Unit,
    variableScript: String,
    onVariableScriptChange: (String) -> Unit,
    onDelete: () -> Unit,
    onPaste: (String) -> Unit,
    filesDir: File?,
    memoryList: SnapshotStateList<MemoryElement>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(5.dp),
    ) {
        var fullVariableInfoShown by remember { mutableStateOf(false) }
        var dropdownMenuShown by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .padding(5.dp),
        ) {

            if (fullVariableInfoShown) {
                Dialog(
                    onDismissRequest = { fullVariableInfoShown = false },
                ) {
                    Surface(
                        modifier = Modifier
                            .requiredWidth(LocalConfiguration.current.screenWidthDp.dp * 0.95f)
                            .requiredHeight(LocalConfiguration.current.screenHeightDp.dp * 0.95f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .requiredWidth(LocalConfiguration.current.screenWidthDp.dp * 0.92f)
                                    .align(Alignment.CenterHorizontally)
                                    .weight(1f),
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .requiredWidth(LocalConfiguration.current.screenWidthDp.dp * 0.92f)
                                        .padding(vertical = 5.dp)
                                        .fillMaxHeight()
                                        .verticalScroll(rememberScrollState())
                                        .horizontalScroll(rememberScrollState()),
                                    value = variableScript,
                                    onValueChange = onVariableScriptChange,
                                    textStyle = TextStyle(
                                        fontSize = 16.sp,
                                    ),
                                    colors = TextFieldDefaults.textFieldColors(
                                        // TODO: change TextFieldDefaults
                                        //backgroundColor = Color.White,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                )

                                val context = LocalContext.current
                                Button(
                                    modifier = Modifier
                                        //.weight(0.2f)
                                        .align(alignment = Alignment.BottomCenter)
                                        .padding(5.dp),
                                    onClick = {
                                        onVariableChange(
                                            eval(
                                                memoryList,
                                                filesDir,
                                                variableScript,
                                                context
                                            )
                                        )
                                    },
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_keyboard_double_arrow_down_24),
                                        contentDescription = null
                                    )
                                }
                            }

                            TextField(
                                modifier = Modifier
                                    .requiredWidth(LocalConfiguration.current.screenWidthDp.dp * 0.92f)
                                    .align(Alignment.CenterHorizontally)
                                    .verticalScroll(rememberScrollState())
                                    .horizontalScroll(rememberScrollState())
                                    .weight(1f)
                                    .padding(bottom = 5.dp),
                                value = variable,
                                onValueChange = onVariableChange,
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    //backgroundColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                )
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f),
                    value = variableName,
                    onValueChange = onVariableNameChange,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
                )
                Spacer(
                    modifier = Modifier
                        .size(5.dp)
                        .weight(0.5f)
                )
                BasicTextField(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(9f)
                        .verticalScroll(rememberScrollState()),
                    value = variable,
                    onValueChange = onVariableChange,
                    textStyle = TextStyle(fontSize = 16.sp),
                )
                Spacer(modifier = Modifier.size(5.dp))
                IconButton(modifier = Modifier
                    .width(24.dp)
                    .weight(1f),
                    onClick = { dropdownMenuShown = true }
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "")
                }
            }
            DropdownMenu(
                offset = DpOffset(x = 999.dp, y = 0.dp), // Alignment.End
                modifier = Modifier.align(Alignment.TopEnd),
                expanded = dropdownMenuShown,
                onDismissRequest = { dropdownMenuShown = false },
            ) {
                DropdownMenuItem(
                    {
                        dropdownMenuShown = false
                        fullVariableInfoShown = true
                    }
                ) {
                    Text(LocalContext.current.getString(R.string.view_full))
                }
                DropdownMenuItem(
                    {
                        dropdownMenuShown = false
                        onPaste(variable)
                    },
                ) {
                    Text(LocalContext.current.getString(R.string.paste))
                }
                DropdownMenuItem(
                    {
                        dropdownMenuShown = false
                        onDelete()
                    }
                ) {
                    Text(LocalContext.current.getString(R.string.delete))
                }
            }
        }
    }
}

@Composable
fun Page0(
    memoryList: SnapshotStateList<MemoryElement>,
    onPaste: (String) -> Unit,
    filesDir: File?,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.align(Alignment.TopCenter)) {
            items(memoryList.size) { i ->
                MemoryElementCard(
                    variable = memoryList[i].variable,
                    onVariableChange = {
                        memoryList[i] = MemoryElement(
                            memoryList[i].variableName,
                            it,
                            memoryList[i].variableScript
                        )
                    },
                    variableName = memoryList[i].variableName,
                    onVariableNameChange = {
                        memoryList[i] = MemoryElement(
                            it,
                            memoryList[i].variable,
                            memoryList[i].variableScript
                        )
                    },
                    variableScript = memoryList[i].variableScript,
                    onVariableScriptChange = {
                        memoryList[i] = MemoryElement(
                            memoryList[i].variableName,
                            memoryList[i].variable,
                            it
                        )
                    },
                    onDelete = { memoryList.removeAt(i) },
                    onPaste = onPaste,
                    filesDir = filesDir,
                    memoryList = memoryList,
                )
            }
        }
    }
}

/*@Preview(showBackground = true, widthDp = 360, heightDp = 640, fontScale = 1f)
@Composable
fun Preview() {

}*/