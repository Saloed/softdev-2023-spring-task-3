package com.example.dailyplanner.appbotnav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailyplanner.Plan
import com.example.dailyplanner.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

//@Composable
//fun rememberFirebaseAuthLauncher(
//    onAuthComplete: (AuthResult) -> Unit,
//    onAuthError: (ApiException) -> Unit
//): ManagedActivityResultLauncher<Intent, ActivityResult> {
//    val scope = rememberCoroutineScope()
//    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)!!
//            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
//            scope.launch {
//                val authResult = Firebase.auth.signInWithCredential(credential).await()
//                onAuthComplete(authResult)
//            }
//        } catch (e: ApiException) {
//            onAuthError(e)
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Plans(
    plans: List<Plan>,
    onAddPlan: (Plan) -> Unit,
    viewModel: PlansViewModel = viewModel(),
    onCheckPlan : (Plan) -> Unit
) {

    val ref = FirebaseAuth.getInstance()
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formattedDate by remember {
        derivedStateOf { DateTimeFormatter.ofPattern("dd MMM yyyy").format(pickedDate) }
    }
    val dateDialogState = rememberMaterialDialogState()
    val openDialog = remember { mutableStateOf(false) }
    val openProfile = remember { mutableStateOf(false) }
    val openAuth = remember { mutableStateOf(false) }
    val isSignIn = remember { mutableStateOf(false) }

    var text by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }


    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val timeDialogState = rememberMaterialDialogState()
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(pickedTime)
        }
    }
    val isCheckedHabbit = remember { mutableStateOf(false) }
    val isRegistred = remember { mutableStateOf(false) }

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
                onClick = { openDialog.value = true },
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


        RecyclerView(currentDay = formattedDate, viewModel = viewModel(), onCheckPlan =onCheckPlan )
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
            initialTime = LocalTime.NOON,
            title = "Pick a time",
            timeRange = LocalTime.MIDNIGHT..LocalTime.NOON
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
                Column() {

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
                        value = text,
                        onValueChange = { text = it }
                    )

                    Row(modifier = Modifier.wrapContentSize()) {
                        Checkbox(
                            checked = isCheckedHabbit.value,
                            onCheckedChange = { isCheckedHabbit.value = it })
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
                            openDialog.value = false;
                            onAddPlan(
                                Plan(
                                    formattedDate,
                                    formattedTime to text,
                                    isCheckedHabbit.value,
                                    false

                                )
                            );
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
            Button(
                onClick = {
                    openAuth.value = true


//                    ref.createUserWithEmailAndPassword(
//                    email.trim(),
//                    password.trim())

                },
                colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color.Black)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Person",
                    Modifier.size(30.dp)
                )
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
                Text(text = "Профиль")
            },
            text = {
                Text(text = email.text)
                Text(text = "За сегодня вы выполнили ${viewModel.HabbitCheckedPlans(formattedDate)} полезных привычек")
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
                            openProfile.value = false;
                        }
                    ) {
                        Text("Закрыть")
                    }
                }
            }
        )

    }
    if (openAuth.value) {
        if (!isSignIn.value) {
            AlertDialog(
                onDismissRequest = {
                    openAuth.value = false
                },
                title = {
                    Text(text = "Вход/регистрация")
                },
                modifier = Modifier
                    .width(900.dp)
                    .height(350.dp), // установка ширины и высоты диалога
                text = {
                    Column() {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Ваш email")
                        OutlinedTextField(
                            label = { Text(text = "email") },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            value = email,
                            onValueChange = { email = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Text(text = "Пароль")
                        OutlinedTextField(
                            label = { Text(text = "password") },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Checkbox(
                            checked = isRegistred.value,
                            onCheckedChange = { isRegistred.value = it })
                    }

                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Green,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                isSignIn.value = true;
                                openAuth.value = false;
                                if (isRegistred.value) {
                                    ref.signInWithEmailAndPassword(
                                        email.text,
                                        password.text
                                    )
                                } else {
                                    ref.createUserWithEmailAndPassword(
                                        email.text,
                                        password.text
                                    )
                                    ref.signInWithEmailAndPassword(
                                        email.text,
                                        password.text
                                    )

                                }
                            }
                        ) {
                            Text("Войти")
                        }
                    }
                }
            )

        } else {
            AlertDialog(
                onDismissRequest = {
                    openAuth.value = false
                },
                title = {
                    Text(text = "Вы уже вошли: ${email.text}")
                },
                modifier = Modifier
                    .width(900.dp)
                    .height(350.dp), // установка ширины и высоты диалога
                text = {
                    Column() {

                        Button(onClick = { ref.signOut(); isSignIn.value = false }) {
                        Text(text = "Выйти из аккаунта")
                        }

                    }

                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Green,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                openAuth.value = !openAuth.value
                            }
                        ) {
                            Text("Закрыть")
                        }
                    }
                }
            )


        }
    }
}


//    @Composable
//    fun Profile() {
//        Image(
//            painter = painterResource(
//                id = R.drawable.prof_image
//            ),
//            contentDescription = "Image",
//            modifier = Modifier.size(150.dp)
//        )
//        Column(
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.End,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Text(text = "Степан Сергеев")
//
//        }
//    }
//}
@Composable
fun ListItem(plan: Plan, viewModel: PlansViewModel, onCheckPlan: (Plan) -> Unit) {
    val expanded = remember { mutableStateOf(false) }

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
                    Text(text = "Time")
                    Text(text = plan.time_planText.first, style = MaterialTheme.typography.h5)

                }

                    Checkbox(checked = viewModel.getCurrentPlan(plan).planDone, onCheckedChange = {viewModel.planDone(plan)})
            }


                Column(
                    modifier = Modifier.padding(
                        bottom = planText.coerceAtLeast(0.dp)
                    )

                ) {

                    Text(text = plan.time_planText.second)
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
fun RecyclerView(currentDay: String, viewModel: PlansViewModel = viewModel(), onCheckPlan: (Plan) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 55.dp)
    ) {
        items(items = viewModel.getCurrentDayPlans(currentDay)) { time ->
            ListItem(
                Plan(
                    currentDay,
                    time.time_planText, time.useful_habit,
                    time.planDone
                ),

                viewModel = viewModel,
                onCheckPlan = onCheckPlan
            )
        }
    }

}

