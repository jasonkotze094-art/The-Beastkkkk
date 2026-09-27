package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Beast Dark Theme Core
val DarkBackground = Color(0xFF090C10)
val DarkSurface = Color(0xFF101620)
val DarkSurfaceVariant = Color(0xFF161F2E)
val DarkSurfaceBorder = Color(0xFF223147)

// Cyber Blue Preset (Default from video)
val CyberBluePrimary = Color(0xFF00E5FF)
val CyberBlueSecondary = Color(0xFF0070F3)
val CyberBlueAccent = Color(0xFF00B0FF)
val CyberBlueGlow = Color(0x3300E5FF)

// Neon Pink Preset
val NeonPinkPrimary = Color(0xFFFF007F)
val NeonPinkSecondary = Color(0xFFB000FF)
val NeonPinkAccent = Color(0xFFFF4081)
val NeonPinkGlow = Color(0x33FF007F)

// Matrix Green Preset
val MatrixGreenPrimary = Color(0xFF00E676)
val MatrixGreenSecondary = Color(0xFF00B0FF)
val MatrixGreenAccent = Color(0xFF76FF03)
val MatrixGreenGlow = Color(0x3300E676)

// Sunset Preset
val SunsetPrimary = Color(0xFFFF5722)
val SunsetSecondary = Color(0xFFFF9800)
val SunsetAccent = Color(0xFFFFC107)
val SunsetGlow = Color(0x33FF5722)

// Royal Gold Preset
val RoyalGoldPrimary = Color(0xFFFFD700)
val RoyalGoldSecondary = Color(0xFFDAA520)
val RoyalGoldAccent = Color(0xFFFFA000)
val RoyalGoldGlow = Color(0x33FFD700)

// Status Colors
val BeastSuccess = Color(0xFF00E676)
val BeastError = Color(0xFFFF1744)
val BeastWarning = Color(0xFFFFB300)
val BeastBuy = Color(0xFF00E676)
val BeastSell = Color(0xFFFF3366)
val TextPrimary = Color(0xFFF0F6FC)
val TextSecondary = Color(0xFF8B949E)
val TextMuted = Color(0xFF586069)

enum class BeastThemePreset(val displayName: String, val primary: Color, val secondary: Color, val accent: Color) {
    CYBER_BLUE("Cyber Blue", CyberBluePrimary, CyberBlueSecondary, CyberBlueAccent),
    NEON_PINK("Neon Pink", NeonPinkPrimary, NeonPinkSecondary, NeonPinkAccent),
    MATRIX_GREEN("Matrix Green", MatrixGreenPrimary, MatrixGreenSecondary, MatrixGreenAccent),
    SUNSET("Sunset", SunsetPrimary, SunsetSecondary, SunsetAccent),
    ROYAL_GOLD("Royal Gold", RoyalGoldPrimary, RoyalGoldSecondary, RoyalGoldAccent)
}
