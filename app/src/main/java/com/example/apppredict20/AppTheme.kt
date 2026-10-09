package com.example.apppredict20

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PetrolBlue = Color(0xFF0F4C5C)
val PetrolBlueDark = Color(0xFF0B3948)
val SoftTeal = Color(0xFF2A9D8F)

val AppBackground = Color(0xFFF5F7FA)
val CardBackground = Color(0xFFFFFFFF)

val TextPrimary = Color(0xFF1F2937)
val TextSecondary = Color(0xFF6B7280)

val WarningColor = Color(0xFFF59E0B)
val ErrorColor = Color(0xFFDC2626)
val SuccessColor = Color(0xFF2E8B57)

private val AppColorScheme = lightColorScheme(
    primary = PetrolBlue,
    onPrimary = Color.White,

    secondary = SoftTeal,
    onSecondary = Color.White,

    background = AppBackground,
    onBackground = TextPrimary,

    surface = CardBackground,
    onSurface = TextPrimary,

    error = ErrorColor,
    onError = Color.White
)

@Composable
fun AppPredictTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}