package com.example.dreamscape.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = TextOnDark,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = PrimaryBlueLight,

    secondary = Gray400,
    onSecondary = TextOnDark,
    secondaryContainer = Gray700,
    onSecondaryContainer = TextOnDark,

    tertiary = InfoBlueLight,
    onTertiary = TextOnDark,
    tertiaryContainer = InfoBlue,
    onTertiaryContainer = TextOnDark,

    error = ErrorRedLight,
    onError = TextOnDark,
    errorContainer = ErrorRed,
    onErrorContainer = TextOnDark,

    background = DarkBackground,
    onBackground = TextOnDark,

    surface = DarkSurface,
    onSurface = TextOnDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    outline = InputBorder,
    outlineVariant = DividerDark,

    scrim = OverlayColor,
    inverseSurface = Gray200,
    inverseOnSurface = TextPrimary,
    inversePrimary = PrimaryBlue,

    surfaceDim = Gray900,
    surfaceBright = Gray700,
    surfaceContainerLowest = DarkBackground,
    surfaceContainerLow = DarkSurface,
    surfaceContainer = DarkSurfaceVariant,
    surfaceContainerHigh = Gray600,
    surfaceContainerHighest = Gray500
)

@Composable
fun DreamscapeTheme(
    darkTheme: Boolean = true, // Default to dark theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme  // Fixed: now uses DarkColorScheme
        else -> DarkColorScheme  // Use dark even in light mode
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false // Always dark status bar
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object DreamscapeColors {
    val primary = PrimaryBlue
    val background = DarkBackground
    val surface = DarkSurface
    val surfaceVariant = DarkSurfaceVariant
    val textPrimary = TextOnDark
    val textSecondary = TextSecondary
    val textTertiary = TextTertiary
    val inputBackground = InputFieldBackground
    val inputBorder = InputBorder
    val success = SuccessGreen
    val warning = WarningYellow
    val error = ErrorRed
    val info = InfoBlue
    val divider = DividerDark
    val shadow = ShadowColor
}