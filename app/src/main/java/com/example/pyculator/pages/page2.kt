package com.example.pyculator.pages

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.pyculator.R
import com.example.pyculator.compile
import java.io.File

val keywordsRegex = Regex(
    """\s(False|None|True|__peg_parser__|and|as|assert|async|await|break|class|
    |continue|def|del|elif|else|except|finally|for|from|global|if|import|in|is|lambda|nonlocal|not|
    |or|pass|raise|return|try|while|with|yield)(:|\s)""".trimMargin()
)
val functionsRegex = Regex(
    """\s(abs|aiter|all|any|anext|ascii|bin|bool|breakpoint|bytearray|bytes|
    |callable|chr|classmethod|compile|complex|delattr|dict|dir|divmod|enumerate|eval|exec|filter|
    |float|format|frozenset|getattr|globals|hasattr|hash|help|hex|id|input|int|isinstance|
    |issubclass|iter|len|list|locals|map|max|memoryview|min|next|object|oct|open|ord|pow|
    |print|property|range|repr|reversed|round|set|setattr|slice|sorted|staticmethod|str|
    |sum|super|tuple|type|vars|zip|__import__)\s""".trimMargin()
)
val operatorsRegex = Regex(
    """=|
        |==|!=|<|<=|>|>=|
        |\+|-|\*|/|//|%|\*\*|
        |\+=|-=|\*=|/=|%=|
        |^|\||&|~|>>|<<""".trimMargin()
)
val braces = mapOf(
    '(' to ')', '{' to '}', '[' to ']',
)
val bracesRegex = Regex("""[(){}\[\]]""")
val strRegex = Regex("""([rfbu]?'[^'\\\n]*(\\.[^'\\\n]*)*')|([rfbu]?"[^"\\\n]*(\\.[^"\\\n]*)*")""")
val bigStrRegex = Regex("") // TODO()
val numericRegex = Regex("""(\b[+-]?[0-9_]+[lL]?\b)
    ||(\b[+-]?0[xX][0-9A-Fa-f_]+[lL]?\b)
    ||(\b[+-]?[0-9_]+(?:\.[0-9_]+)?(?:[eE][+-]?[0-9_]+)?\b)""".trimMargin()
) // Decimal Hex Float

val commentRegex = Regex("""#.*""")

fun highlight(text: String, errorString: String): AnnotatedString {

    val errorStringSplit = errorString.split(" ")
    val errorLine = if (errorString == "") -1 else errorStringSplit[errorStringSplit.size - 2].toInt()
    val errorChar = if (errorString == "") -1 else errorStringSplit.last().toInt()

    val styles = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    // Extremely inefficient
    for (i in keywordsRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Magenta), i.range.first, i.range.last+1))
    }
    for (i in functionsRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Red), i.range.first, i.range.last+1))
    }
    for (i in operatorsRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Yellow), i.range.first, i.range.last+1))
    }
    for (i in bracesRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Yellow), i.range.first, i.range.last+1))
    }
    for (i in strRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Green), i.range.first, i.range.last+1))
    }
    for (i in bigStrRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Green), i.range.first, i.range.last+1))
    }
    for (i in numericRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Yellow), i.range.first, i.range.last+1))
    }
    for (i in commentRegex.findAll(text)) {
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Gray), i.range.first, i.range.last+1))
    }

    if (errorString.isNotEmpty()) {
        var line = 1
        var charInLine = 1
        var totalChar = 0
        for (i in text) {
            charInLine++
            totalChar++
            if (i == '\n') {
                line++
                charInLine = 1
            }
            if (line == errorLine && charInLine == errorChar) {
                styles.add(
                    AnnotatedString.Range(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = Color.Red
                        ), totalChar, totalChar + 1
                    )
                )
            }
        }
    }




    // Braces checker
    val s = mutableListOf<Pair<Char, Int>>()
    var skip = false
    for ((i, c) in text.withIndex()) {
        // Commented braces don't count
        if (c == '#') skip = true
        if (c == '\n') skip = false
        if (skip) continue

        if (braces.containsKey(c)) {
            s.add((c to i))
        } else if (s.isNotEmpty() && braces[s.last().first] == c) {
            s.removeLast()
        } else if (braces.containsValue(c)) {
            // Closing brace has no pair
            styles.add(AnnotatedString.Range(SpanStyle(color = Color.Red), i, i+1))
        }
    }
    for (i in s) {
        // Opening brace has no pair
        styles.add(AnnotatedString.Range(SpanStyle(color = Color.Red), i.second, i.second+1))
    }


    return AnnotatedString(
        text = text,
        spanStyles = styles
    )
}

class Highlighting(val errorString: String): VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            // Do NOT neither change width nor skip any characters here
            highlight(text.text, errorString),
            OffsetMapping.Identity
        )
    }
}
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

@Composable
fun Page2(
    toCompile: (String) -> String,
    context: Context,
    filesDir: File?,
) {
    val initialExecInput = stringResource(R.string.code_example)
    var errorString = ""
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

    val pxInDp = context.resources.displayMetrics.densityDpi/160f

    val scrollState = rememberScrollState()


    @Composable
    fun linesUsed(context: Context, line: String, lineNumerationWidth: Int): Int {
        val scriptFieldWidth =
            context.resources.displayMetrics.widthPixels - lineNumerationWidth

        return androidx.compose.ui.text.Paragraph(
            text = line,
            style = MaterialTheme.typography.body1,
            constraints = Constraints(maxWidth = scriptFieldWidth),
            density = LocalDensity.current,
            fontFamilyResolver = LocalFontFamilyResolver.current,
        ).lineCount
    }

    MeasureUnconstrainedViewWidth(
        viewToMeasure = {
            BasicTextField(
                modifier = Modifier.width(IntrinsicSize.Min),
                value = "1",
                onValueChange = {},
                textStyle = MaterialTheme.typography.body1
            )
        }
    ) { oneCharWidth ->
        val lineNumerationWidth =
            oneCharWidth * 3 + pxInDp

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
                textStyle = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Right
                ),
                modifier = Modifier
                    .width(
                        (lineNumerationWidth/pxInDp).dp
                    )
                    .verticalScroll(scrollState),
                onTextLayout = { textLayoutResult -> textLayoutResult.lineCount },
            )

            Spacer(modifier = Modifier.width(3.dp))
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxHeight().width(1.dp)
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
                    textStyle = MaterialTheme.typography.body1.copy(
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