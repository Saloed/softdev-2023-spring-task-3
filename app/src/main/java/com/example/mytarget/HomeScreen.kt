package com.example.mytarget

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.ColorPalette
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTargetApp(
    onAddTask: (Task) -> Unit,
    viewModel: TasksViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var textFieldHeader by remember { mutableStateOf("") }
    var textFieldDescription by remember { mutableStateOf("") }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedDateCalendar by remember { mutableStateOf(LocalDate.now()) }
    val dateDialogState = rememberMaterialDialogState()
    val calendarDialogState = rememberMaterialDialogState()
    val colorDialogState = rememberMaterialDialogState()
    var pickedColor by remember { mutableStateOf(Color.Blue) }
    var showSortDialog by remember{ mutableStateOf(false) }
    var sortType by remember{ mutableStateOf(TypeOfSort.DAY) }


    val resetFields: () -> Unit = {
        textFieldHeader = ""
        textFieldDescription = ""
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Login()

            when (sortType) {
                TypeOfSort.DAY -> Text(DateTimeFormatter.ofPattern("dd MMM yyyy").format(pickedDateCalendar), modifier = Modifier.padding(0.dp,16.dp))
                TypeOfSort.WEEK -> Text(stringResource(id = R.string.taskOnWeek), modifier = Modifier.padding(0.dp,16.dp))
                else -> Text(stringResource(id = R.string.taskOnMonth), modifier = Modifier.padding(0.dp,16.dp))
            }

            Button(
                onClick = {calendarDialogState.show()},
                colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color.Black)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = "Calendar",
                    Modifier.size(40.dp)
                )
            }
        }

        MaterialDialog(
            dialogState = calendarDialogState,
            buttons = {
                positiveButton(stringResource(id = R.string.choose)){

                }
                negativeButton(stringResource(id = R.string.cancel)){

                }
            }
        ){
            datepicker(
                initialDate = LocalDate.now(),
                title = stringResource(id = R.string.whichDate)
            ) {
                pickedDateCalendar = it
            }
        }
        TaskList(viewModel = viewModel(), date = pickedDateCalendar, sort = sortType)
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = {showSortDialog = true},
                        colors = ButtonDefaults.buttonColors(
                            Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_sort_24),
                            contentDescription = "West",
                            Modifier.size(40.dp)
                        )
                    }

                    Button(onClick = { showDialog = true }, shape = CircleShape)
                    { Text(stringResource(id = R.string.plus), fontSize = 40.sp) }
                }
            }
        }

        if (showSortDialog) {
            AlertDialog(
                onDismissRequest = {showSortDialog = false},
                title = { Text(stringResource(id = R.string.typeSort))},
                text = {
                    Column(verticalArrangement = Arrangement.Top) {
                        Button(onClick = {sortType = TypeOfSort.DAY},
                            colors = ButtonDefaults.buttonColors(
                                Color.White,
                                contentColor = Color.Black
                            )) {
                            Text(stringResource(id = R.string.day))
                        }
                        Button(onClick = {sortType = TypeOfSort.WEEK},
                            colors = ButtonDefaults.buttonColors(
                                Color.White,
                                contentColor = Color.Black
                            )) {
                            Text(stringResource(id = R.string.week))
                        }
                        Button(onClick = {sortType = TypeOfSort.MONTH},
                            colors = ButtonDefaults.buttonColors(
                                Color.White,
                                contentColor = Color.Black
                            )) {
                            Text(stringResource(id = R.string.month))
                        }
                    }
                },
            confirmButton = {
                Button(onClick = { showSortDialog = false }) {
                    Text(stringResource(id = R.string.ok))
                }
            })
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(id = R.string.addTask)) },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = textFieldHeader,
                            onValueChange = { textFieldHeader = it },
                            singleLine = true,
                            placeholder = { stringResource(id = R.string.taskName) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = textFieldDescription,
                            onValueChange = { textFieldDescription = it },
                            placeholder = { stringResource(id = R.string.taskDescription) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = { dateDialogState.show() }) {
                            Text(stringResource(id = R.string.chooseDate1))
                        }

                        Button(onClick = { colorDialogState.show() }) {
                            Text(stringResource(id = R.string.chooseColor))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                    }
                    MaterialDialog(
                        dialogState = dateDialogState,
                        buttons = {
                            positiveButton(stringResource(id = R.string.choose)){

                            }
                            negativeButton(stringResource(id = R.string.cancel)){

                            }
                        }
                    ){
                        datepicker(
                            initialDate = LocalDate.now(),
                            title = stringResource(id = R.string.chooseDate)

                        ) {
                            pickedDate = it
                        }
                    }

                    MaterialDialog(dialogState = colorDialogState,
                        buttons = {
                            positiveButton(stringResource(id = R.string.choose)){

                            }
                            negativeButton(stringResource(id = R.string.cancel)){

                            }
                        }) {
                        colorChooser(colors = ColorPalette.Primary) {
                            pickedColor = it
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onAddTask(
                                Task((viewModel.tasks.size).toLong(),
                                textFieldHeader,
                                textFieldDescription,
                                pickedDate,
                                mutableStateOf(false),
                                pickedColor)
                            )
                            showDialog = false
                            resetFields()
                            pickedColor = Color.Blue
                            pickedDate = LocalDate.now()
                        },
                        enabled = textFieldHeader.isNotEmpty()
                    ) {
                        Text(stringResource(id = R.string.ok))
                    }
                }
            )
        }
    }
}

@Composable
fun Login() {
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    val token = stringResource(id = R.string.web_client_id)
    val context = LocalContext.current
    if (user == null) {
        Button(
            onClick = {
                val gso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
            },
            colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Person",
                Modifier.size(40.dp)
            )
        }
    }
    else {
        Button(onClick = {
            Firebase.auth.signOut()
            user = null
        }, colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color.Black)) {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_off_24),
                contentDescription = "Person",
                Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
):ManagedActivityResultLauncher<Intent, ActivityResult>{
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("GoogleAuth", "account $account")
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            Log.d("GoogleAuth", e.toString())
            onAuthError(e)
        }
    }
}

@Composable
fun TasksListItem(
    task: Task,
    viewModel: TasksViewModel
) {
    var showDialogDescription by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember{ mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(color = task.taskColor.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            viewModel.getTaskByTask(task)?.isComplete?.let { it -> Checkbox(checked = it.value, onCheckedChange = { viewModel.taskIsSuccessful(task.id, it) }, modifier = Modifier.weight(1f)) }

            IconButton(onClick = { showDialogDescription = true }, modifier = Modifier.weight(1f)) {
                Icon(imageVector = Icons.Default.MoreVert , contentDescription = "description")
            }

            Text(text = task.taskName, modifier = Modifier.weight(7f))

            IconButton(onClick = { showDeleteConfirm = true }, modifier = Modifier.weight(1f)) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        }


    }
    if (showDialogDescription) {
        AlertDialog(
            onDismissRequest = { showDialogDescription = false },
            title = { stringResource(id = R.string.description) },
            text = {
                Text(text = task.taskDescription)
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialogDescription = false

                    }
                ) {
                    Text(stringResource(id = R.string.ok))
                }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { stringResource(id = R.string.approve) },
            text = {
                Text(stringResource(id = R.string.approveDel))
            },
            confirmButton = {
                Button(
                    onClick = {

                        showDeleteConfirm = false
                        viewModel.removeTask(task.id)

                    }
                ) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDeleteConfirm = false
                }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun TaskList(viewModel: TasksViewModel,date: LocalDate, sort: TypeOfSort) {
    LazyColumn {
        if (sort == TypeOfSort.DAY) {
            items(items = viewModel.getTaskByDate(date)) { task ->
                TasksListItem(
                    Task(task.id,
                        task.taskName,
                        task.taskDescription,
                        task.date,
                        task.isComplete,
                        task.taskColor),
                    viewModel = viewModel
                )
            }
        }

        if (sort == TypeOfSort.WEEK) {
            items(items = viewModel.getTasksForCurrentWeek(viewModel.tasks)) { task ->
                TasksListItem(
                    Task(task.id,
                        task.taskName,
                        task.taskDescription,
                        task.date,
                        task.isComplete,
                        task.taskColor),
                    viewModel = viewModel
                )
            }
        }

        if (sort == TypeOfSort.MONTH) {
            items(items = viewModel.getTasksForCurrentMonth(viewModel.tasks)) { task ->
                TasksListItem(
                    Task(task.id,
                        task.taskName,
                        task.taskDescription,
                        task.date,
                        task.isComplete,
                        task.taskColor),
                    viewModel = viewModel
                )
            }
        }
    }
}

