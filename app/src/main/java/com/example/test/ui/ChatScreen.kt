package com.example.test.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.test.R
import com.example.test.data.ChatElement
import com.example.test.data.ChatType
import com.example.test.data.Message
import com.example.test.data.RecipientsData
import com.example.test.data.UserPreferencesRepository
import com.example.test.ui.theme.Purple80
import com.example.test.ui.theme.PurpleGrey80
import java.io.FileNotFoundException

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    chat: ChatElement,
    modifier: Modifier = Modifier,
    userPreferencesRepository: UserPreferencesRepository,
    sendAction: (String) -> Unit
) {
    val meId: String
    var membersListVisible by remember { mutableStateOf(false) }
    when (chat.type) {
        ChatType.DISCORD -> meId =
            userPreferencesRepository.discordId.collectAsState(initial = "").value

        ChatType.TELEGRAM -> meId =
            userPreferencesRepository.telegramId.collectAsState(initial = "").value

        else -> meId = ""
    }
    Scaffold() {
//        innerPadding-> val uiState by viewModel.uiState.collectAsState()
        Button(
            onClick = { membersListVisible = !membersListVisible },
            modifier = Modifier
                .absolutePadding(left = 150.dp)
                .zIndex(5.0f)
        ) {
            Text("Список участников")
        }
        if (membersListVisible) {
            recipientsList(chat.recipients)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (lazyColumn, messageBar) = createRefs()
//            Spacer(modifier = Modifier.padding(16.dp))
                LazyColumn(
                    reverseLayout = true,
                    modifier = Modifier.constrainAs(lazyColumn) { bottom.linkTo(messageBar.top) }) {

                    items(chat.messages) { message ->
                        Box(modifier = modifier.padding(4.dp)) {
                            val fontSize =
                                userPreferencesRepository.fontSize.collectAsState(initial = 16).value
                            messageDisplay(
                                message,
                                contentFontSize = fontSize,
                                isMe = message.author.id == meId
                            )
                        }
                    }

                }

                newMessageBar(
                    onSendButtonClicked = { sendAction(it) },
                    modifier = Modifier.constrainAs(messageBar) {
                        bottom.linkTo(parent.bottom, margin = 4.dp)
                    }
                )
            }
        }
    }
}

@Composable
fun newMessageBar(onSendButtonClicked: (String) -> Unit, modifier: Modifier = Modifier) {
    var message by remember { mutableStateOf("") }
    Row(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        IconButton(onClick = {
            onSendButtonClicked(message)
            message = ""
        }, modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = stringResource(R.string.submit)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
//    ChatScreen()
}

@Composable
fun messageDisplay(
    message: Message,
    modifier: Modifier = Modifier,
    contentFontSize: Int,
    isMe: Boolean
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (!isMe) com.example.test.ui.theme.Purple80 else com.example.test.ui.theme.PurpleGrey80)
    ) {

        Column(modifier = modifier.padding(horizontal = 8.dp)) {
            Text(
                text = message.content,
                fontSize = contentFontSize.sp
            )
            Row {
                Text(
                    text = message.sender,
                    fontWeight = FontWeight.Thin
                )
                Text(text = message.timestamp.toString(), fontWeight = FontWeight.Thin,)
            }

            if (message.previewBitmap != null || message.imagePath != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    var bitmap: Bitmap
                    if (message.imagePath != null && message.imagePath != "") {
                        try {
                            bitmap = BitmapFactory.decodeFile(message.imagePath)
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                            bitmap = BitmapFactory.decodeByteArray(
                                message.previewBitmap,
                                0,
                                message.previewBitmap!!.size
                            )
                        }
                    } else {
                        bitmap = BitmapFactory.decodeByteArray(
                            message.previewBitmap,
                            0,
                            message.previewBitmap!!.size
                        )
                    }
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .height(400.dp)
                            .width(300.dp)
                    )

                }
            }
        }


    }

}

@Composable
fun recipientsList(recipients: List<RecipientsData>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = PurpleGrey80)
            .zIndex(5.0f)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.align(Alignment.TopCenter)
        ) {
            items(recipients) { recipient ->
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = recipient.username,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

        }
    }
}