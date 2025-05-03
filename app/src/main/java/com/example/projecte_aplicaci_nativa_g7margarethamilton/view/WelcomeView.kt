package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale

@Composable
fun WelcomeView(navController: NavController, viewModel: UserViewModel) {
    val context = LocalContext.current
    val selectedLang by viewModel.langCodeField.collectAsState()
    val themeModeField by viewModel.themeModeField.collectAsState()

    val languageOptions = listOf(
        "ca" to "🇦🇩",
        "es" to "🇪🇸",
        "en" to "🇬🇧"
    )

    var dropdownExpanded by remember { mutableStateOf(false) }
    var userHasSelectedLang by remember { mutableStateOf(false) }

    // ✅ Només forcem anglès si no ho hem fet mai
    LaunchedEffect(Unit) {
        if (!viewModel.hasForcedEnglish(context)) {
            viewModel.langCodeField.value = "en"
            context.setLocale("en")
            viewModel.saveLanguageToPrefs(context, "en")
            viewModel.loadSettings(context)
            viewModel.markForcedEnglish(context)
            userHasSelectedLang = true
        } else {
            viewModel.loadSettings(context)
            userHasSelectedLang = viewModel.hasUserChosenLanguage(context)
        }
    }

    val localizedContext = context.setLocale(selectedLang)

    val selectedEmoji = if (userHasSelectedLang) {
        languageOptions.firstOrNull { it.first == selectedLang }?.second ?: "🌐"
    } else {
        "🌐"
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 48.dp, bottom = 140.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = localizedContext.getString(R.string.welcome_view_title),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = localizedContext.getString(R.string.welcome_view_subtitle),
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Flow2Day!", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(64.dp))

                Button(
                    onClick = { navController.navigate(Routes.Login.route) },
                    modifier = Modifier.fillMaxWidth(0.85f).height(50.dp)
                ) {
                    Text(
                        text = localizedContext.getString(R.string.welcome_view_login_button),
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate(Routes.Register.route) },
                    modifier = Modifier.fillMaxWidth(0.85f).height(50.dp)
                ) {
                    Text(
                        text = localizedContext.getString(R.string.welcome_view_register_button),
                        fontSize = 16.sp
                    )
                }
            }

            // 🌍 Idioma i tema
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clickable { dropdownExpanded = true }
                        .padding(8.dp)
                ) {
                    Text(
                        text = selectedEmoji,
                        fontSize = 32.sp
                    )

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        languageOptions.forEach { (code, emoji) ->
                            DropdownMenuItem(
                                text = { Text(text = emoji, fontSize = 28.sp) },
                                onClick = {
                                    viewModel.langCodeField.value = code
                                    viewModel.saveLanguageToPrefs(context, code)
                                    context.setLocale(code)
                                    viewModel.loadSettings(context)
                                    dropdownExpanded = false
                                    userHasSelectedLang = true
                                }
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = if (themeModeField) "🌙" else "☀️",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Switch(
                        checked = themeModeField,
                        onCheckedChange = { viewModel.themeModeField.value = it }
                    )
                }
            }
        }
    }
}
