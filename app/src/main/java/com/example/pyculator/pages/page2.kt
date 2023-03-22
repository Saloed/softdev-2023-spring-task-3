package com.example.pyculator.pages

import android.content.Context
import android.util.TypedValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.pyculator.R
import java.io.File




@Composable
fun Page2(
    context: Context,
    filesDir: File?,
) {
    val initialExecInput = stringResource(R.string.code_example)
    var execInput by remember {
        mutableStateOf(
            if (filesDir != null) {
                val file = File(filesDir, "toExec.py")
                val reader = file.reader()
                reader.readText().also { reader.close() }
            } else {
                initialExecInput
            }
        )
    }

    val pxInSp = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        1f,
        context.resources.displayMetrics
    ).toInt()
    val pxInDp = context.resources.displayMetrics.densityDpi/160

    // (pixels used for 3 numbers) + one dp used for divider
    val reservedForLineNumeration =
        MaterialTheme.typography.body1.fontSize.value * pxInSp * 3 + pxInDp

    @Composable
    fun linesUsed(context: Context, line: String): Int {
        val textWidthConstraint =
            context.resources.displayMetrics.widthPixels - reservedForLineNumeration.toInt()

        return androidx.compose.ui.text.Paragraph(
            text = line,
            style = MaterialTheme.typography.body1,
            constraints = Constraints(maxWidth = textWidthConstraint),
            density = LocalDensity.current,
            fontFamilyResolver = LocalFontFamilyResolver.current,
        ).lineCount
    }

    Row {
        val linesNumerationBuilder = StringBuilder()
        val scrollState = rememberScrollState()

        for ((i, line) in execInput.split('\n').withIndex()) {
            val lineString = if (i < 1000) {
                (i + 1).toString()
            } else {
                "#" + (i+1)%1000
            }
            linesNumerationBuilder.append(lineString)
            linesNumerationBuilder.append("\n".repeat(linesUsed(context, line)))
        }

        BasicTextField(
            value = linesNumerationBuilder.toString(),
            enabled = false,
            onValueChange = { },
            textStyle = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.onSurface
            ),
            modifier = Modifier
                .width(
                    (reservedForLineNumeration*160/context.resources.displayMetrics.densityDpi).dp
                )
                .verticalScroll(scrollState),
            onTextLayout = {textLayoutResult -> textLayoutResult.lineCount }
        )
        // Divider
        Box(
            Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(15f)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    // .horizontalScroll(rememberScrollState())
                    .verticalScroll(scrollState),

                value = execInput,
                onValueChange = { execInput = it },
                textStyle = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onSurface
                ),
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                onClick = {
                    if (filesDir != null) {
                        val file = File(filesDir, "toExec.py")
                        file.createNewFile()
                        file.writer().use { it.write(execInput) }
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            ) {
                Icon(painterResource(R.drawable.baseline_save_24), null)
            }
        }
    }
}