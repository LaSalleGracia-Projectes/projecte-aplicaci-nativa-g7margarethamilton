package com.example.projecte_aplicaci_nativa_g7margarethamilton.View

import android.content.Context
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class Task(
    val timeStart: String,
    val timeEnd: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isGray: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyTasksView(navController: NavController) {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("es"))

    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(
                    "8:00",
                    "8:30",
                    "Estudiar React",
                    "Repasar hooks y estado global",
                    false,
                    true
                ),
                Task("9:00", "9:30", "Ejercicio Matutino", "30 minutos de cardio", false, false),
                Task(
                    "10:00",
                    "10:45",
                    "Reunión de Equipo",
                    "Planificación sprint semanal",
                    false,
                    true
                ),
                Task("11:00", "12:00", "Desarrollo Frontend", "Implementar nueva UI", false, false),
                Task("14:00", "15:30", "Clase de Inglés", "Preparar presentación oral", false, true)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Close button
            IconButton(
                onClick = { /* TODO: Handle navigation */ },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Black
                )
            }

            // Title and date
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "Tasques del día",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = currentDate.format(formatter),
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            // Calendar button
            Button(
                onClick = { /* TODO: Handle calendar */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E3B4E)
                )
            ) {
                Text("Calendari")
            }
        }

        // Tasks list
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

@Composable
fun TaskItem(task: Task) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Time column
        Column(
            modifier = Modifier
                .width(60.dp)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = task.timeStart,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = task.timeEnd,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Task content
        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Color.LightGray)
                .background(if (task.isGray) Color(0xFFF5F5F5) else Color.White)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.description,
                    fontSize = 14.sp,
                    color = Color.Gray
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
                            checkedColor = Color(0xFF2E3B4E),
                            uncheckedColor = Color.Gray,

                            )
                    )
                    // Delete icon
                    IconButton(
                        onClick = { /* TODO: Handle Delete click */ },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar tarea",
                            tint = Color.Gray
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