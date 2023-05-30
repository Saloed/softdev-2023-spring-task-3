package com.example.dailyplanner.appbotnav

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailyplanner.Plan
import com.example.dailyplanner.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Plans(
    viewModel: PlansViewModel = viewModel(),
    onCheckPlan: (String) -> Unit,
    onAddNotify: NotificationManager
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadPlans()
    }
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formattedDate by remember {
        derivedStateOf { DateTimeFormatter.ofPattern("dd MMM yyyy").format(pickedDate) }
    }
    val dateDialogState = rememberMaterialDialogState()
    val openDialog = remember { mutableStateOf(false) }
    val openChangeDialog = remember { mutableStateOf(false) }
    val openProfile = remember { mutableStateOf(false) }

    var pickedTime by remember {
        mutableStateOf(LocalTime.now(Clock.systemDefaultZone()))
    }
    val timeDialogState = rememberMaterialDialogState()
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(pickedTime)
        }
    }
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    val token = stringResource(R.string.default_web_client_id)
    val context = LocalContext.current
    var idGenerator = remember { "0".toInt() }
    val NOTIFICATION_ID = 999
    val CHANNEL_ID = "channelID"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel.
        val name = stringResource(R.string.channel_name)
        val descriptionText = stringResource(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        onAddNotify.createNotificationChannel(mChannel)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            modifier = Modifier.height(16.dp),
            progress = viewModel.daysCheckedPlans(formattedDate),
            color = Color.Green
        )
        Row(
            modifier = Modifier
                .width(220.dp)
                .height(35.dp)
        ) {


            Button(
                onClick = { dateDialogState.show() }, modifier = Modifier
                    .size(width = 150.dp, height = 35.dp)
                    .padding(start = 10.dp), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {

                Text(
                    text = formattedDate,
                    modifier = Modifier.padding(0.dp),
                    style = MaterialTheme.typography.body2
                )
            }
            Button(
                onClick = { openDialog.value = true; idGenerator++; },
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .size(width = 60.dp, height = 35.dp)
                    .padding(start = 10.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.add_button
                    ),
                    contentDescription = "Image",
                    modifier = Modifier.size(20.dp)
                )
            }

        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
        }


        RecyclerView(
            currentDay = formattedDate,
            viewModel = viewModel(),
            onCheckPlan = onCheckPlan,
            openChangeDialog = openChangeDialog
        )
    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
            }
            negativeButton(text = "Cancel")
        }
    ) {
        timepicker(

            title = "Pick a time",
            is24HourClock = true
        ) {
            pickedTime = it
        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }) {
        datepicker(
            initialDate = LocalDate.now(),
            title = ("Выберите день"),
        ) {
            pickedDate = it
        }

    }




    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Добавить план")
            },
            text = {
                Column {

                    Button(
                        onClick = { timeDialogState.show() },
                        modifier = Modifier
                            .size(height = 40.dp, width = 70.dp)
                            .padding(bottom = 5.dp), colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = formattedTime, style = MaterialTheme.typography.body2)
                    }
                    TextField(
                        value = viewModel.planUiState.planText,
                        onValueChange = { viewModel.onTextChange(it) }
                    )

                    Row(modifier = Modifier.wrapContentSize()) {
                        Checkbox(
                            checked = viewModel.planUiState.useful_habit,
                            onCheckedChange = { viewModel.onHabitChange(it) })
                        Text(text = "Полезная привычка", modifier = Modifier.padding(top = 10.dp))
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Green,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            viewModel.onTimeChange(formattedTime)
                            viewModel.onDateChange(formattedDate)
                            openDialog.value = false
                            viewModel.addPlan()
                            viewModel.loadPlans()
                            val intent = Intent(context, Plan::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_MUTABLE
                            )

                            val startOfDay = LocalDateTime.of(pickedDate, LocalTime.MIN)
                            val millis =
                                startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("План через час")
                                .setContentText("Вы запланировали через час ${viewModel.planUiState.planText}")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setWhen(pickedTime.toNanoOfDay() / 1000000 + millis - 1000 * 60 * 60)
                                .setContentIntent(pendingIntent)
                                .setGroupSummary(true)


                            with(NotificationManagerCompat.from(context)) {
                                notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
                            }


                        }
                    ) {
                        Text("Добавить")
                    }
                }
            }
        )

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (user == null) {
                Button(onClick = {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.google_logo
                        ),
                        contentDescription = "Image",
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Button(onClick = {
                    viewModel.signOut()
                    user = null
                }) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.google_logo
                        ),
                        contentDescription = "Image",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Button(
                onClick = { openProfile.value = true },
                shape = RoundedCornerShape(10),
                modifier = Modifier
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.profile_icon
                    ),
                    contentDescription = "Image",
                    modifier = Modifier.size(20.dp)
                )
            }

        }

    }




    if (openProfile.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            modifier = Modifier.wrapContentSize(),
            title = {
                Text(
                    if (user != null) user!!.displayName.toString() else "Войдите в аккаунт",
                    style = MaterialTheme.typography.h5,
                    textDecoration = TextDecoration.Underline
                )
            },
            text = {
                Text(text = "За сегодня вы выполнили ${viewModel.habitCheckedPlans(formattedDate)} полезных привычек")
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Green,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            openProfile.value = false
                        }
                    ) {
                        Text("Закрыть")
                    }
                }
            }
        )

    }
}

@Composable
fun ListItem(
    plan: Plan,
    viewModel: PlansViewModel,
    onCheckPlan: (String) -> Unit,
    openChangeDialog: MutableState<Boolean>
) {
    val expanded = remember { mutableStateOf(false) }
    val checkboxChanged = remember {
        mutableStateOf(false)
    }
    val planText by animateDpAsState(
        if (expanded.value) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )
    Surface(
        color = Color.LightGray,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(9.dp)
                .fillMaxWidth(),
        ) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = plan.time, style = MaterialTheme.typography.h5)

                }

                Checkbox(
                    checked = viewModel.getPlan(plan.documentId).planDone,
                    onCheckedChange = { viewModel.getPlan(plan.documentId).planDone = it; checkboxChanged.value = true }
                )
//
            }
            if (checkboxChanged.value) {
                viewModel.updateNote(
                    viewModel.getPlan(plan.documentId).documentId,
                )
                checkboxChanged.value = false
            }
            Column(
                modifier = Modifier.padding(
                    bottom = planText.coerceAtLeast(0.dp)
                )

            ) {

                Text(text = plan.planText)
                if (plan.useful_habit)
                    Text(
                        text = "Полезная привычка",
                        style = MaterialTheme.typography.h6,
                        color = Color.Green
                    )
            }
        }
    }

}

//"$day - ${month + 1} - $year"

@Composable
fun RecyclerView(
    currentDay: String,
    viewModel: PlansViewModel = viewModel(),
    onCheckPlan: (String) -> Unit,
    openChangeDialog: MutableState<Boolean>
) {

    LazyColumn(
        modifier = Modifier
            .padding(bottom = 55.dp)
    ) {
        items(items = viewModel.getCurrentDayPlans(currentDay)) { plan ->
            ListItem(
                Plan(
                    plan.userId,
                    currentDay,
                    plan.time, plan.planText, plan.useful_habit,
                    plan.planDone, plan.documentId
                ),

                viewModel = viewModel,
                onCheckPlan = onCheckPlan,
                openChangeDialog = openChangeDialog
            )
        }
    }

}

