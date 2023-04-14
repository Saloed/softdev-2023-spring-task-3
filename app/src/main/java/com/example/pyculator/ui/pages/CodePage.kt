package com.example.pyculator.ui.pages

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pyculator.R

import com.example.pyculator.utils.Highlighting
import com.example.pyculator.utils.compile
import java.io.File


@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Int) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints()).width

        val contentPlaceable = subcompose("content") {
            content(measuredWidth)
        }[0].measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun CodePage(
    context: Context,
    filesDir: File?,
    codeFontSize: Int,
) {
    val textStyle = MaterialTheme.typography.body1.copy(
        fontSize = codeFontSize.sp,
        lineBreak = LineBreak.Simple
    )
    val initialExecInput = stringResource(R.string.code_example)
    var execInput by remember {
        mutableStateOf(
            if (filesDir != null) {
                val file = File(filesDir, "toExec.py")
                if (file.createNewFile()) file.writeText(initialExecInput)
                val reader = file.reader()
                reader.readText().also { reader.close() }
            } else {
                initialExecInput
            }
        )
    }
    var errorString by remember { mutableStateOf(compile(execInput)) }

    val pxInDp = context.resources.displayMetrics.densityDpi/160f

    val scrollState = rememberScrollState()


    @Composable
    fun linesUsed(context: Context, line: String, lineNumerationWidth: Int): Int {
        val scriptFieldWidth =
            context.resources.displayMetrics.widthPixels - lineNumerationWidth

        return Paragraph(
            text = line,
            style = textStyle,
            constraints = Constraints(maxWidth = scriptFieldWidth),
            density = LocalDensity.current,
            fontFamilyResolver = LocalFontFamilyResolver.current,
        ).lineCount
    }

    MeasureUnconstrainedViewWidth(
        viewToMeasure = {
            BasicTextField(
                modifier = Modifier.width(IntrinsicSize.Min),
                value = "_",
                onValueChange = {},
                textStyle = textStyle,
            )
        }
    ) { oneCharWidth ->
        val lineNumerationWidth =
            oneCharWidth * 3 + 4*pxInDp

        Row {
            val linesNumerationBuilder = StringBuilder()

            for ((i, line) in execInput.split('\n').withIndex()) {
                val lineString = if (i < 1000) {
                    (i + 1).toString()
                } else {
                    "#" + (i+1)%1000
                }
                linesNumerationBuilder.append(lineString)
                linesNumerationBuilder.append("\n".repeat(linesUsed(context, line, lineNumerationWidth.toInt())))
            }

            BasicTextField(
                value = linesNumerationBuilder.toString(),
                enabled = false,
                onValueChange = { },
                textStyle = textStyle.copy(
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Right
                ),
                modifier = Modifier
                    .width(
                        (lineNumerationWidth / pxInDp).dp
                    )
                    .verticalScroll(scrollState),
                onTextLayout = { textLayoutResult -> textLayoutResult.lineCount },
            )

            Spacer(modifier = Modifier.width(3.dp))
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))

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
                    textStyle = textStyle.copy(
                        color = MaterialTheme.colors.onSurface
                    ),
                    visualTransformation = Highlighting(errorString)
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
                        errorString = compile(execInput)
                        //println(errorString)
                    },
                    backgroundColor = Color.Transparent,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                ) {
                    Icon(painterResource(R.drawable.baseline_save_24), null)
                }
            }
        }
    }

}