package com.example.myapplication.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
)

private val LightColorPalette = lightColors(
        /* default colors to override
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Teal200
        background = Color.White,
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        */
)

@Composable
fun PyculatorTheme(theme: String, content: @Composable () -> Unit) {
    val colors = when (theme) {
        "dark" -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}