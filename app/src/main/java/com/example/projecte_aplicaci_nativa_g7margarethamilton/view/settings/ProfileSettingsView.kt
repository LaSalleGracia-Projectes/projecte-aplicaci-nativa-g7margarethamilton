// File: view/settings/ProfileSettingsView.kt
package com.example.projecte_aplicaci_nativa_g7margarethamilton.view.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.BottomNavBar
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsView(
    navController: NavController,
    viewModel: UserViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Carreguem settings un cop en iniciar la vista
    LaunchedEffect(Unit) { viewModel.loadSettings() }

    // Estats locals per al dropdown d'idioma
    var expandedLanguage by remember { mutableStateOf(false) }
    val languageOptions = listOf("ca", "es", "en")
    val selectedLanguage by viewModel.langCodeField.collectAsState()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(top = 40.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Configuració de perfil",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.updateSettings() }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Guardar",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Modo oscuro
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Modo oscuro")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.themeModeField.collectAsState().value,
                    onCheckedChange = { viewModel.themeModeField.value = it }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown d'idioma
            ExposedDropdownMenuBox(
                expanded = expandedLanguage,
                onExpandedChange = { expandedLanguage = !expandedLanguage },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedLanguage,
                    onValueChange = {},
                    label = { Text("Idioma") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedLanguage) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedLanguage,
                    onDismissRequest = { expandedLanguage = false }
                ) {
                    languageOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.langCodeField.value = option
                                expandedLanguage = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Notificacions
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Notificacions")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.allowNotificationField.collectAsState().value,
                    onCheckedChange = { viewModel.allowNotificationField.value = it }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Merge calendari
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Unir calendari d'horaris")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.mergeScheduleCalendarField.collectAsState().value,
                    onCheckedChange = { viewModel.mergeScheduleCalendarField.value = it }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Missatges d'estat
            viewModel.settingsMsg.collectAsState().value?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }
            viewModel.settingsError.collectAsState().value?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
