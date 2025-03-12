package com.seenu.dev.android.echojournal.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00419C),
    primaryContainer = Color(0xFF1F70F5),
    onPrimary = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF001945),
    secondary = Color(0xFF3B4663),
    secondaryContainer = Color(0xFF3B4663),
    onErrorContainer = Color(0xFF680014),
    errorContainer = Color(0xFFFFEDEC),
    onError = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE1E2EC),
    onSurface = Color(0xFF191A20),
    onSurfaceVariant = Color(0xFF40434F),
    inverseOnSurface = Color(0xFFEEF0FF),
    outline = Color(0xFF6C7085),
    outlineVariant = Color(0xFFC1C3CE),
)

@Composable
fun EchoJournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
//        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}