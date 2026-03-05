package com.genpause.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val ZenDarkColorScheme = darkColorScheme(
    primary = ZenPrimary,
    onPrimary = ZenBackground,
    secondary = ZenSecondary,
    onSecondary = ZenTextPrimary,
    tertiary = ZenAccent,
    onTertiary = ZenBackground,
    background = ZenBackground,
    onBackground = ZenTextPrimary,
    surface = ZenSurface,
    onSurface = ZenTextPrimary,
    surfaceVariant = ZenElevatedSurface,
    onSurfaceVariant = ZenTextSecondary,
    error = ZenError,
    onError = ZenTextPrimary,
    outline = ZenDivider,
    outlineVariant = ZenDivider,
    surfaceContainerHigh = ZenElevatedSurface,
    surfaceContainer = ZenSurface,
    surfaceContainerLow = ZenBackground,
)

@Composable
fun GenPauseTheme(content: @Composable () -> Unit) {
    val colorScheme = ZenDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ZenBackground.toArgb()
            window.navigationBarColor = ZenBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ZenTypography,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp),
        ),
        content = content
    )
}
