package com.example.pyculator.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration

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
    val errorLine = if (errorString == "OK") -1 else errorStringSplit[errorStringSplit.size - 2].toInt()
    val errorChar = if (errorString == "OK") -1 else errorStringSplit.last().toInt()

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

    if (errorString != "OK") {
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

class Highlighting(private val errorString: String): VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            // Do NOT neither change width nor skip any characters here
            highlight(text.text, errorString),
            OffsetMapping.Identity
        )
    }
}