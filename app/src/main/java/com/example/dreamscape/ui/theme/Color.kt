package com.example.dreamscape.ui.theme

import androidx.compose.ui.graphics.Color

// Dark Theme - Primary Colors
val PrimaryBlue = Color(0xFF3B82F6) // Clean blue
val PrimaryBlueDark = Color(0xFF1D4ED8)
val PrimaryBlueLight = Color(0xFF60A5FA)

// Background Colors - Dark Theme
val DarkBackground = Color(0xFF121212) // True dark
val DarkSurface = Color(0xFF1E1E1E) // Card/surface color
val DarkSurfaceVariant = Color(0xFF2C2C2C) // Slightly lighter

// For compatibility
val LightBackground = DarkBackground
val CardWhite = DarkSurface
val SurfaceLight = DarkSurfaceVariant

// Text Colors - Dark Theme
val TextPrimary = Color(0xFFFFFFFF) // White
val TextSecondary = Color(0xFFB0B0B0) // Light gray
val TextTertiary = Color(0xFF808080) // Medium gray
val TextDisabled = Color(0xFF606060)
val TextOnDark = Color(0xFFFFFFFF)

// Input Colors - Dark Theme
val InputFieldBackground = Color(0xFF2C2C2C)
val InputBorder = Color(0xFF404040)
val InputBorderFocused = Color(0xFF3B82F6)
val SelectedTabBackground = Color(0xFF2C2C2C)

// Status Colors
val SuccessGreen = Color(0xFF10B981)
val SuccessGreenLight = Color(0xFF34D399)
val WarningYellow = Color(0xFFFBBF24)
val WarningYellowLight = Color(0xFFFDE047)
val ErrorRed = Color(0xFFEF4444)
val ErrorRedLight = Color(0xFFF87171)
val InfoBlue = Color(0xFF3B82F6)
val InfoBlueLight = Color(0xFF60A5FA)

// Grays
val Gray50 = Color(0xFF1E1E1E)
val Gray100 = Color(0xFF2C2C2C)
val Gray200 = Color(0xFF404040)
val Gray300 = Color(0xFF606060)
val Gray400 = Color(0xFF808080)
val Gray500 = Color(0xFF9CA3AF)
val Gray600 = Color(0xFFB0B0B0)
val Gray700 = Color(0xFFC0C0C0)
val Gray800 = Color(0xFFD0D0D0)
val Gray900 = Color(0xFFE0E0E0)

// Divider and Border
val DividerLight = Color(0xFF2C2C2C)
val DividerDark = Color(0xFF2C2C2C)

// Shadow and Overlay
val ShadowColor = Color(0xFF000000).copy(alpha = 0.5f)
val OverlayColor = Color(0xFF000000).copy(alpha = 0.7f)

// Button Colors
val ButtonPrimary = PrimaryBlue
val ButtonPrimaryHover = PrimaryBlueDark
val ButtonSecondary = DarkSurfaceVariant
val ButtonSecondaryHover = Gray200
val ButtonDisabled = Color(0xFF2C2C2C)

// For compatibility with existing red-themed code
val PrimaryRed = PrimaryBlue
val PrimaryRedLight = PrimaryBlueLight
val PrimaryRedDark = PrimaryBlueDark

// Legacy colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)