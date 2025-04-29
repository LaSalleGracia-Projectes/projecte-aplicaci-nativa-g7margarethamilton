// File: MainActivity.kt
package com.example.projecte_aplicaci_nativa_g7margarethamilton

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.ui.theme.Projecteaplicacinativag7margarethamiltonTheme
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.EntryPoint
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel

@SuppressLint("NewApi")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = UserViewModel()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HideStatusBar()

            val navController = rememberNavController()
            val darkTheme by viewModel.themeModeField.collectAsState()

            Projecteaplicacinativag7margarethamiltonTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EntryPoint(navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun HideStatusBar() {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window ?: return@SideEffect
        ViewCompat.getWindowInsetsController(view)?.let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
