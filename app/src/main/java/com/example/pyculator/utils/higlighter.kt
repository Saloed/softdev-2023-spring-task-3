package com.example.pyculator.utils

import Python3Lexer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import org.antlr.v4.runtime.*

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
) = when(token.type) {
        Python3Lexer.STRING -> Color.Green

        Python3Lexer.DEF,
        Python3Lexer.RETURN,
        Python3Lexer.RAISE,
        Python3Lexer.FROM,
        Python3Lexer.IMPORT,
        Python3Lexer.AS,
        Python3Lexer.GLOBAL,
        Python3Lexer.NONLOCAL,
        Python3Lexer.ASSERT,
        Python3Lexer.IF,
        Python3Lexer.ELIF,
        Python3Lexer.ELSE,
        Python3Lexer.WHILE,
        Python3Lexer.FOR,
        Python3Lexer.IN,
        Python3Lexer.TRY,
        Python3Lexer.FINALLY,
        Python3Lexer.WITH,
        Python3Lexer.EXCEPT,
        Python3Lexer.LAMBDA,
        Python3Lexer.OR,
        Python3Lexer.AND,
        Python3Lexer.NOT,
        Python3Lexer.IS,
        Python3Lexer.NONE,
        Python3Lexer.TRUE,
        Python3Lexer.FALSE,
        Python3Lexer.CLASS,
        Python3Lexer.YIELD,
        Python3Lexer.DEL,
        Python3Lexer.PASS,
        Python3Lexer.CONTINUE,
        Python3Lexer.BREAK,
        Python3Lexer.ASYNC,
        Python3Lexer.AWAIT, -> Color.Magenta

        Python3Lexer.OPEN_BRACE,
        Python3Lexer.OPEN_BRACK,
        Python3Lexer.OPEN_PAREN,
        Python3Lexer.CLOSE_BRACE,
        Python3Lexer.CLOSE_BRACK,
        Python3Lexer.CLOSE_PAREN -> Color.Yellow

        Python3Lexer.INTEGER,
        Python3Lexer.BIN_INTEGER,
        Python3Lexer.DECIMAL_INTEGER,
        Python3Lexer.HEX_INTEGER,
        Python3Lexer.OCT_INTEGER,
        Python3Lexer.NUMBER,
        Python3Lexer.FLOAT_NUMBER,
        Python3Lexer.IMAG_NUMBER -> Color.Yellow

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
        Python3Lexer.MOD_ASSIGN,
        Python3Lexer.MULT_ASSIGN,
        Python3Lexer.RIGHT_SHIFT_ASSIGN,
        Python3Lexer.OR_ASSIGN,
        Python3Lexer.XOR_ASSIGN -> Color.Yellow

        else -> null
    }

fun highlight(text: String, errorString: String): AnnotatedString {
    styles.clear()
    val lexer = Python3Lexer( ANTLRInputStream(text) )
    var token: Token
    var color: Color?

    do {
        token = lexer.nextToken()
        color = getColor(token)
        if (color != null) addStyle(color, token)
    } while (token.text != "<EOF>")

    val errorStringSplit = errorString.split(" ")
    val errorLine = if (errorString == "OK") -1 else errorStringSplit[errorStringSplit.size - 2].toInt()
    val errorChar = if (errorString == "OK") -1 else errorStringSplit.last().toInt()

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