package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale
import org.checkerframework.checker.units.qual.s

@Composable
fun WelcomeView(navController: NavController, viewModel: UserViewModel) {
    val context = LocalContext.current
    val selectedLang by viewModel.langCodeField.collectAsState()
    val themeModeField by viewModel.themeModeField.collectAsState()

    // Per mostrar emojis
    val languageOptions = listOf(
        "en" to "🇬🇧",
        "ca" to "\uD83C\uDDE6\uD83C\uDDE9", // o 🇦🇩
        "es" to "🇪🇸"
    )

    var dropdownExpanded by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Títols i botons com abans...

                Text("Welcome", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("to", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Flow2Day!", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(100.dp))

                Button(
                    onClick = { navController.navigate(Routes.Login.route) },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) { Text("Login", fontSize = 16.sp) }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(Routes.Register.route) },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) { Text("Register", fontSize = 16.sp) }
            }

            // 🔽 Idioma + switch a la part inferior
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Idioma amb banderes
                Box {
                    Text(
                        text = languageOptions.find { it.first == selectedLang }?.second ?: "🌐",
                        modifier = Modifier
                            .clickable { dropdownExpanded = true }
                            .padding(12.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.small)
                    )
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        languageOptions.forEach { (code, emoji) ->
                            DropdownMenuItem(
                                text = { Text(emoji) },
                                onClick = {
                                    viewModel.langCodeField.value = code
                                    dropdownExpanded = false
                                    viewModel.saveLanguageToPrefs(context, code)
                                    context.setLocale(code)
                                    viewModel.loadSettings(context)
                                    navController.navigate(Routes.Welcome.route) {
                                        popUpTo(Routes.Welcome.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }

                // Switch/boto bolea
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Bolea")
                    Switch(
                        checked = themeModeField,
                        onCheckedChange = { viewModel.themeModeField.value = it },
                    )
                }
            }
        }
    }
}