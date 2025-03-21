package com.example.projecte_aplicaci_nativa_g7margarethamilton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.EntryPoint
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.ui.theme.Projecteaplicacinativag7margarethamiltonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = UserViewModel()

        setContent {
            val navController = rememberNavController()

            Projecteaplicacinativag7margarethamiltonTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(), // Añade padding para evitar que el contenido se oculte
                    color = MaterialTheme.colorScheme.background
                ) {
                    EntryPoint(navController, viewModel)

                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Projecteaplicacinativag7margarethamiltonTheme {
        Greeting("Android")
    }
}