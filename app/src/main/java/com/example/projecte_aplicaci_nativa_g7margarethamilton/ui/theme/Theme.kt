package com.example.projecte_aplicaci_nativa_g7margarethamilton.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5A4FF3),
    secondary = Color(0xFF00C04D),
    tertiary = Color(0xFFFFBB00),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF25313C),
    onTertiary = Color(0xFF2A3641),
    onBackground = Color(0xFF2C363F),
    onSurface = Color(0xFF6D7D8B),
    error = Color(0xFFED455D),
    onError = Color(0xFFFFFFFF)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5A4FF3),
    secondary = Color(0xFF00C04D),
    tertiary = Color(0xFFFFBB00),
    background = Color(0xFF12171C),
    surface = Color(0xFF1E2A33),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFADADAD),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFDAE3EA),
    error = Color(0xFFED455D),
    onError = Color(0xFF12171C)
)

@Composable
fun Projecteaplicacinativag7margarethamiltonTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
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
