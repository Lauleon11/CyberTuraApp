package me.lauraleon.cybertura.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = CyberBlue,
    secondary = CyberViolet,
    onSecondary = Color.White,
    tertiary = CyberMagenta,
    background = CyberBackground,
    onBackground = Color.White,
    surface = CyberSurface,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CyberBlueLight,
    onPrimary = Color.White,
    secondary = CyberViolet,
    onSecondary = Color.White,
    tertiary = CyberMagenta,
    background = CyberBackgroundLight,
    onBackground = CyberBlue,
    surface = CyberSurfaceLight,
    onSurface = CyberBlue
)

@Composable
fun CyberTuraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
