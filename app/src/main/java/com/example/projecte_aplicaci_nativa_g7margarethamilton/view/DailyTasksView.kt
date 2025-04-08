package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class Task(
    val timeStart: String,
    val timeEnd: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyTasksView(navController: NavController) {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("es"))

    var tasks by remember {
        mutableStateOf(
            listOf(
                Task("8:00", "8:30", "Estudiar React", "Repasar hooks y estado global", false),
                Task("9:00", "9:30", "Ejercicio Matutino", "30 minutos de cardio", false),
                Task("10:00", "10:45", "Reunión de Equipo", "Planificación sprint semanal", false),
                Task("11:00", "12:00", "Desarrollo Frontend", "Implementar nueva UI", false),
                Task("14:00", "15:30", "Clase de Inglés", "Preparar presentación oral", false)
            )
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = { navController.navigate(Routes.Home.route) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Text(
                        text = "Tasques del día",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = currentDate.format(formatter),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { /* TODO: Handle calendar */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "Calendari",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                tasks.forEach { task ->
                    TaskItem(task)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .width(60.dp)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = task.timeStart,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = task.timeEnd,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = task.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { /* TODO: Handle completion */ },
                        modifier = Modifier.align(Alignment.CenterVertically),
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.secondary,
                            uncheckedColor = MaterialTheme.colorScheme.onBackground,
                        )
                    )
                    IconButton(
                        onClick = { /* TODO: Handle Delete click */ },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar tarea",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskPreview() {

    val navController = rememberNavController()
    DailyTasksView(navController)
}