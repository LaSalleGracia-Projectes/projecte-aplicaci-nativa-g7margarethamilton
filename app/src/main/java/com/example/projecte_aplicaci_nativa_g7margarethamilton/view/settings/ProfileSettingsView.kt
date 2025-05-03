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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.BottomNavBar
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsView(
    navController: NavController,
    viewModel: UserViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val lang = viewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    // Carreguem settings un cop en iniciar la vista
    LaunchedEffect(Unit) { viewModel.loadSettings(context) }

    // Estats locals per al dropdown d'idioma
    var expandedLanguage by remember { mutableStateOf(false) }
    val languageOptions = listOf("ca", "es", "en")
    val selectedLanguage by viewModel.langCodeField.collectAsState()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(top = 45.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = localizedContext.getString(R.string.advanced_settings_view_title),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = localizedContext.getString(R.string.common_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.updateSettings(context)
                        navController.navigate(Routes.ProfileSettings.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = localizedContext.getString(R.string.common_save),
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
                Text(localizedContext.getString(R.string.advanced_settings_view_theme))
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
                    label = { Text(localizedContext.getString(R.string.advanced_settings_view_language)) },
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
                Text(localizedContext.getString(R.string.advanced_settings_view_notifications))
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.allowNotificationField.collectAsState().value,
                    onCheckedChange = { viewModel.allowNotificationField.value = it }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Merge calendari
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(localizedContext.getString(R.string.advanced_settings_view_merge_task))
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
