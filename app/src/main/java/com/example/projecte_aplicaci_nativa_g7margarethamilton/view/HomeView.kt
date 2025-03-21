package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes

@Composable
fun HomeView(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Título principal
            Text(
                text = "What we do today?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E3B4E)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Grid de opciones (2x2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón Agenda
                HomeOptionButton(
                    title = "Tareas del día",
                    onClick = { navController.navigate(Routes.DailyTasks.route) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Botón Calendari
                HomeOptionButton(
                    title = "Calendari",
                    onClick = { /* TODO: Navigate to Calendar */ },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón Lista compra
                HomeOptionButton(
                    title = "Lista compra",
                    onClick = { /* TODO: Navigate to Shopping List */ },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Botón Rutines exercici
                HomeOptionButton(
                    title = "Rutines exercici",
                    onClick = { /* TODO: Navigate to Exercise Routines */ },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HomeOptionButton(
                    title = "Agenda",
                    onClick = {/*TODO: navigate to Agenda*/ },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun HomeOptionButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .border(1.dp, Color.LightGray)
            .background(Color(0xFFE3E8F0))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2E3B4E),
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Ir a $title",
            tint = Color(0xFF2E3B4E),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    val navController = rememberNavController()
    HomeView(navController = navController)
}

