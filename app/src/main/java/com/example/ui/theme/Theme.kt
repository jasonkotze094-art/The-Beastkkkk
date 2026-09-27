package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalBeastTheme = staticCompositionLocalOf { BeastThemePreset.CYBER_BLUE }

@Composable
fun TheBeastTheme(
    preset: BeastThemePreset = BeastThemePreset.CYBER_BLUE,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = preset.primary,
        onPrimary = Color.Black,
        primaryContainer = preset.secondary.copy(alpha = 0.25f),
        onPrimaryContainer = preset.primary,
        secondary = preset.secondary,
        onSecondary = Color.White,
        secondaryContainer = preset.secondary.copy(alpha = 0.2f),
        onSecondaryContainer = preset.accent,
        tertiary = preset.accent,
        background = DarkBackground,
        onBackground = TextPrimary,
        surface = DarkSurface,
        onSurface = TextPrimary,
        surfaceVariant = DarkSurfaceVariant,
        onSurfaceVariant = TextSecondary,
        outline = preset.primary.copy(alpha = 0.4f),
        outlineVariant = DarkSurfaceBorder,
        error = BeastError,
        onError = Color.White
    )

    CompositionLocalProvider(LocalBeastTheme provides preset) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
