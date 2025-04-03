package com.example.projecte_aplicaci_nativa_g7margarethamilton.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Light Theme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5A4FF3), // Links or buttons
    secondary = Color(0xFF00C04D), // Buttons, checks, OK
    tertiary = Color(0xFFFFBB00), // Whatever you need
    background = Color(0xFFFFFFFF), // Extra
    surface = Color(0xFFDAE3EA), // Interest text or button text with links
    onPrimary = Color(0xFFFFFFFF), // Extra
    onSecondary = Color(0xFFFFFFFF), // Extra
    onTertiary = Color(0xFF25313C), // Titles
    onBackground = Color(0xFF25313C), // Titles
    onSurface = Color(0xFF6D7D8B), // Common text or subtitles
    error = Color(0xFFED455D), // Error or buttons
    onError = Color(0xFFFFFFFF) // Extra
)

// Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4237A8), // Links or buttons
    secondary = Color(0xFF008035), // Buttons, checks, OK
    tertiary = Color(0xFFB38700), // Whatever you need
    background = Color(0xFF12171C), // Titles
    surface = Color(0xFF46515A), // Common text or subtitles
    onPrimary = Color(0xFFCCCCCC), // Extra
    onSecondary = Color(0xFF12171C), // Titles
    onTertiary = Color(0xFF12171C), // Titles
    onBackground = Color(0xFFCCCCCC), // Extra
    onSurface = Color(0xFFADB8C0), // Interest text or button text with links
    error = Color(0xFFA33041), // Error or buttons
    onError = Color(0xFFCCCCCC) // Extra
)

@Composable
fun Projecteaplicacinativag7margarethamiltonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
