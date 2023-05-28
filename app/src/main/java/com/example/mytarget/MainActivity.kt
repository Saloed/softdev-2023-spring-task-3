package com.example.mytarget

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.RoundedCorner
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.mytarget.ui.theme.MyTargetTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.ColorPalette
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.reflect.KProperty

class MainActivity : ComponentActivity() {
    val tasksViewModel by viewModels<TasksViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTargetTheme {
                MyTargetApp(
                    tasksViewModel.tasks,
                    onAddTask = {tasksViewModel.addTask(it)},
                    onClearTasks = {tasksViewModel.clearTasks()},
                    onMarkTask = { tasksViewModel.taskIsSuccesful(it.id, it.isComplete.value) },
                    onRemoveTask = {tasksViewModel.removeTask(it.id,it)},
                    viewModel = tasksViewModel

                )

            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTargetApp(
//) {
//    var showDialog by remember { mutableStateOf(false) }
//    var textFieldHeader by remember { mutableStateOf("") }
//    var textFieldDescription by remember { mutableStateOf("") }
//    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
//    var pickedDateCalendar by remember { mutableStateOf(LocalDate.now()) }
//    val dateDialogState = rememberMaterialDialogState()
//    val calendarDialogState = rememberMaterialDialogState()
//    val colorDialogState = rememberMaterialDialogState()
//    val context = LocalContext.current
//    var pickedColor by remember { mutableStateOf(Color.Unspecified) }
//
//    val resetFields: () -> Unit = {
//        textFieldHeader = ""
//        textFieldDescription = ""
//    }
//
//    Column(Modifier.fillMaxSize()) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.Top
//        ) {
//              Login()
//
//            Button(
//                onClick = {calendarDialogState.show()},
//                colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color.Black)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
//                    contentDescription = "Calendar",
//                    Modifier.size(40.dp)
//                )
//            }
//        }
//
//        MaterialDialog(
//            dialogState = calendarDialogState,
//            buttons = {
//                positiveButton("Выбрать"){
//
//                }
//                negativeButton("Отмена"){
//
//                }
//            }
//        ){
//            datepicker(
//                initialDate = LocalDate.now(),
//                title = "Выберите на какое число показать задачи"
//            ) {
//                pickedDateCalendar = it
//            }
//        }
//
//        Box(Modifier.fillMaxSize()) {
//            Column(
//                Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(bottom = 16.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceAround,
//                    verticalAlignment = Alignment.Bottom
//                ) {
//                    Button(
//                        onClick = {},
//                        colors = ButtonDefaults.buttonColors(
//                            Color.White,
//                            contentColor = Color.Black
//                        )
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.baseline_west_24),
//                            contentDescription = "West",
//                            Modifier.size(40.dp)
//                        )
//                    }
//
//                    Button(onClick = { showDialog = true }, shape = CircleShape)
//                    { Text("+", fontSize = 40.sp) }
//                    Button(
//                        onClick = {},
//                        colors = ButtonDefaults.buttonColors(
//                            Color.White,
//                            contentColor = Color.Black
//                        )
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.baseline_east_24),
//                            contentDescription = "East",
//                            Modifier.size(40.dp)
//                        )
//                    }
//                }
//            }
//        }
//
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                title = { Text(text = "Добавить задачу") },
//                text = {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        TextField(
//                            value = textFieldHeader,
//                            onValueChange = { textFieldHeader = it },
//                            singleLine = true,
//                            placeholder = { Text("Название задачи") }
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        TextField(
//                            value = textFieldDescription,
//                            onValueChange = { textFieldDescription = it },
//                            placeholder = { Text("Описание задачи") }
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Button(onClick = { dateDialogState.show() }) {
//                            Text(text = "Выберите дату")
//                        }
//
//                        Button(onClick = { colorDialogState.show() }) {
//                            Text(text = "Выберите цвет")
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                    }
//                    MaterialDialog(
//                        dialogState = dateDialogState,
//                        buttons = {
//                            positiveButton("Выбрать"){
//
//                            }
//                            negativeButton("Отмена"){
//
//                            }
//                        }
//                    ){
//                        datepicker(
//                            initialDate = LocalDate.now(),
//                            title = "Выбор даты"
//
//                        ) {
//                            pickedDate = it
//                        }
//                    }
//
//                    MaterialDialog(dialogState = colorDialogState,
//                        buttons = {
//                            positiveButton("Выбрать"){
//
//                            }
//                            negativeButton("Отмена"){
//
//                            }
//                        }) {
//                        colorChooser(colors = ColorPalette.Primary) {
//                            pickedColor = it
//                        }
//                    }
//                },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            showDialog = false
//                            resetFields()
//                        }
//                    ) {
//                        Text(text = "OK")
//                    }
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun Login() {
//    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
//    val launcher = rememberFirebaseAuthLauncher(
//        onAuthComplete = { result ->
//            user = result.user
//        },
//        onAuthError = {
//            user = null
//        }
//    )
//    val token = stringResource(id = R.string.web_client_id)
//    val context = LocalContext.current
//    if (user == null) {
//        Button(
//            onClick = {
//                val gso =
//                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(token)
//                        .requestEmail()
//                        .build()
//                val googleSignInClient = GoogleSignIn.getClient(context, gso)
//                launcher.launch(googleSignInClient.signInIntent)
//            },
//            colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color.Black)
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.baseline_person_24),
//                contentDescription = "Person",
//                Modifier.size(40.dp)
//            )
//        }
//    }
//    else {
//        Button(onClick = {
//            Firebase.auth.signOut()
//            user = null
//        }) {
//            Image(
//                painter = painterResource(id = R.drawable.baseline_person_off_24),
//                contentDescription = "Person",
//                Modifier.size(40.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun rememberFirebaseAuthLauncher(
//    onAuthComplete: (AuthResult) -> Unit,
//    onAuthError: (ApiException) -> Unit
//):ManagedActivityResultLauncher<Intent, ActivityResult>{
//    val scope = rememberCoroutineScope()
//    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)!!
//            Log.d("GoogleAuth", "account $account")
//            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
//            scope.launch {
//                val authResult = Firebase.auth.signInWithCredential(credential).await()
//                onAuthComplete(authResult)
//            }
//        } catch (e: ApiException) {
//            Log.d("GoogleAuth", e.toString())
//            onAuthError(e)
//        }
//    }
//}