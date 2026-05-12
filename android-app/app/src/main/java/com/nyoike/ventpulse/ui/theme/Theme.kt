package com.nyoike.ventpulse.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VentPulseColorScheme = lightColorScheme(
    primary = BrandPurple,
    onPrimary = Color.White,
    secondary = SecondaryAccent,
    onSecondary = PrimaryText,
    tertiary = EmotionalPink,
    onTertiary = PrimaryText,
    background = MainBackground,
    onBackground = PrimaryText,
    surface = Color.White,
    onSurface = PrimaryText,
    surfaceVariant = SecondaryBackground,
    onSurfaceVariant = SecondaryText,
    outline = SecondaryAccent.copy(alpha = 0.55f),
    error = AngryMood,
    onError = Color.White
)

@Composable
fun VentPulseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VentPulseColorScheme,
        typography = Typography,
        content = content
    )
}
