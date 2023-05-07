package com.example.pyculator.utils

import Python3BaseListener
import Python3Lexer
import Python3Parser
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import org.antlr.v4.runtime.*

private val functionsRegex = Regex(
    """\s(abs|aiter|all|any|anext|ascii|bin|bool|breakpoint|bytearray|bytes|
    |callable|chr|classmethod|compile|complex|delattr|dict|dir|divmod|enumerate|eval|exec|filter|
    |float|format|frozenset|getattr|globals|hasattr|hash|help|hex|id|input|int|isinstance|
    |issubclass|iter|len|list|locals|map|max|memoryview|min|next|object|oct|open|ord|pow|
    |print|property|range|repr|reversed|round|set|setattr|slice|sorted|staticmethod|str|
    |sum|super|tuple|type|vars|zip|__import__)\s""".trimMargin()
)
private val commentRegex = Regex("""#.*""")

private val styles = mutableSetOf<AnnotatedString.Range<SpanStyle>>()

private fun addStyle(spanStyle: SpanStyle, start: Int, end: Int) {
    styles.add(
        AnnotatedString.Range(
            spanStyle,
            start,
            end,
        )
    )
}
private fun addStyle(color: Color, start: Int, end: Int) {
    addStyle(SpanStyle(color = color), start, end)
}

private fun addStyle(color: Color, token: Token) {
    addStyle(color, token.startIndex, token.stopIndex + 1)
}
private fun addStyle(color: Color, range: IntRange) {
    addStyle(color, range.first, range.last + 1)
}


private fun getColor(
    token: Token,
): Color? {
    var color: Color? = null
    val type = token.type
    if (type == Python3Lexer.STRING) { // Strings
        color = Color.Green
    }
    if (type in Python3Lexer.DEF..Python3Lexer.AWAIT) { // Keywords
        color = Color.Magenta
    }
    if (type in setOf(
            Python3Lexer.OPEN_BRACE,
            Python3Lexer.OPEN_BRACK,
            Python3Lexer.OPEN_PAREN,
            Python3Lexer.CLOSE_BRACE,
            Python3Lexer.CLOSE_BRACK,
            Python3Lexer.CLOSE_PAREN,
        )
    ) {
        color = Color.Yellow
    }
    if (type in setOf(
            Python3Lexer.INTEGER,
            Python3Lexer.BIN_INTEGER,
            Python3Lexer.DECIMAL_INTEGER,
            Python3Lexer.HEX_INTEGER,
            Python3Lexer.OCT_INTEGER,
            Python3Lexer.NUMBER,
            Python3Lexer.FLOAT_NUMBER,
            Python3Lexer.IMAG_NUMBER,
        )
    ) {
        color = Color.Yellow
    }
    if (type in setOf(
            Python3Lexer.STAR,
            Python3Lexer.POWER,
            Python3Lexer.ASSIGN,
            Python3Lexer.OR_OP,
            Python3Lexer.XOR,
            Python3Lexer.AND_OP,
            Python3Lexer.LEFT_SHIFT,
            Python3Lexer.RIGHT_SHIFT,
            Python3Lexer.ADD,
            Python3Lexer.MINUS,
            Python3Lexer.DIV,
            Python3Lexer.MOD,
            Python3Lexer.IDIV,
            Python3Lexer.LESS_THAN,
            Python3Lexer.GREATER_THAN,
            Python3Lexer.EQUALS,
            Python3Lexer.LT_EQ,
            Python3Lexer.GT_EQ,
            Python3Lexer.NOT_EQ_1,
            Python3Lexer.NOT_EQ_2,
            Python3Lexer.AT,
            Python3Lexer.ADD_ASSIGN,
            Python3Lexer.AND_ASSIGN,
            Python3Lexer.SUB_ASSIGN,
            Python3Lexer.POWER_ASSIGN,
            Python3Lexer.AT_ASSIGN,
            Python3Lexer.DIV_ASSIGN,
            Python3Lexer.IDIV_ASSIGN,
            Python3Lexer.LEFT_SHIFT_ASSIGN,
            Python3Lexer.DIV_ASSIGN,
            Python3Lexer.MOD_ASSIGN,
            Python3Lexer.MULT_ASSIGN,
            Python3Lexer.RIGHT_SHIFT_ASSIGN,
            Python3Lexer.OR_ASSIGN,
            Python3Lexer.XOR_ASSIGN,
        )
    ) {
        color = Color.Yellow
    }
    return color
}

class HighlighterListener() : Python3BaseListener() {
    override fun enterEveryRule(ctx: ParserRuleContext?) {
        super.enterEveryRule(ctx)
        val token = ctx?.start
        if (token != null) {
            val color = getColor(token)
            if (color != null) addStyle(color, token)
        }
    }
}



fun highlight(text: String, errorString: String): AnnotatedString {
    val parser = Python3Parser( CommonTokenStream( Python3Lexer( ANTLRInputStream(text) ) ) )
    val listener = HighlighterListener()
    parser.addParseListener(listener)
    parser.file_input()


    val errorStringSplit = errorString.split(" ")
    val errorLine = if (errorString == "OK") -1 else errorStringSplit[errorStringSplit.size - 2].toInt()
    val errorChar = if (errorString == "OK") -1 else errorStringSplit.last().toInt()

    for (i in functionsRegex.findAll(text)) addStyle(Color.Red, i.range)
    for (i in commentRegex.findAll(text)) addStyle(Color.Gray, i.range)

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
                addStyle(
                    SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Red),
                    totalChar,
                    totalChar + 1
                )
            }
        }
    }
    return AnnotatedString(
        text = text,
        spanStyles = styles.toList()
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