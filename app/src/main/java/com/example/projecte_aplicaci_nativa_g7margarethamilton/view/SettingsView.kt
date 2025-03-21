package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ajustes",
                        fontSize = 30.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.Black
                ),
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            SettingOption("Terminis i condicions") { /* TODO: Navigate to Terms */ }
            SettingOption("Sobre nosaltres") { /* TODO: Navigate to About Us */ }
            SettingOption("Contacta") { /* TODO: Navigate to Contact */ }
            SettingOption("Config. Perfil") { /* TODO: Navigate to Profile Config */ }
        }
    }
}

@Composable
fun SettingOption(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2E3B4E)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Ir a $title",
            tint = Color(0xFF2E3B4E)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsViewPreview() {
    val navController = rememberNavController()
    SettingsView(navController)
}