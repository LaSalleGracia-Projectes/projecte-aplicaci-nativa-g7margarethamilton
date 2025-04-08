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
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.ScheduleViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleView(
    navController: NavController,
    userViewModel: UserViewModel,
    viewModel: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
        ScheduleViewModel(userViewModel)
    }
) {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("es"))

    val schedules by viewModel.schedules.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()
    //añadirDatosPrueba(viewModel, userViewModel)

    // Cargar los schedules cuando se inicia la vista
    LaunchedEffect(currentUser) {
        currentUser?.email?.let { email ->
            viewModel.loadSchedules(email)
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Close button
                IconButton(
                    onClick = { navController.navigate(Routes.Home.route) },
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
                        text = "Tareas del día",
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
                    onClick = { navController.navigate(Routes.Calendar.route) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E3B4E)
                    )
                ) {
                    Text("Calendario")
                }
            }

            // Loading state
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Error state
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Tasks list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (schedules.isEmpty()) {
                    Text(
                        text = "No tienes tareas programadas para hoy.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    Text(
                        text = "Tareas programadas:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    // Filtrar las tareas del día actual
                    val todayTasks = schedules.flatMap { schedule ->
                        schedule.tasks.filter { task ->
                            // Aquí deberías implementar la lógica para filtrar las tareas del día actual
                            true // Por ahora mostramos todas las tareas
                        }
                    }

                    todayTasks.forEach { task ->
                        TaskItem(task)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }
        }
    }
}

fun añadirDatosPrueba(
    viewModel: ScheduleViewModel,
    userViewModel: UserViewModel
): List<Schedule_task> {
    val email = userViewModel.currentUser.value?.email.toString()
    viewModel.createNewSchedule(
        "Agenda de prueba",
        email,
        1
    )

    // Esperar un momento para que se cree la agenda
    Thread.sleep(1000)

    val scheduleId = viewModel.schedules.value.firstOrNull { it.email == email }?.id.toString()

    if (scheduleId.isNotEmpty()) {
        viewModel.addTaskToSchedule(
            scheduleId,
            "Tarea de prueba 1",
            "Contenido de la tarea de prueba 1",
            "08:00",
            "09:00",
            1,
            1,
            email
        )
        viewModel.addTaskToSchedule(
            scheduleId,
            "Tarea de prueba 2",
            "Contenido de la tarea de prueba 2",
            "10:00",
            "11:00",
            2,
            1,
            email
        )
    }

    return viewModel.schedules.value.firstOrNull { it.id.toString() == scheduleId }?.tasks
        ?: emptyList()
}

@Composable
fun TaskItem(task: Schedule_task) {
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
                text = task.start_time,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = task.end_time,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Task content
        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Color.LightGray)
                .background(Color.White)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.content,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = { /* TODO: Implementar eliminación */ },
                        modifier = Modifier.size(24.dp)
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
    val userViewModel = androidx.lifecycle.viewmodel.compose.viewModel<UserViewModel>()
    ScheduleView(navController, userViewModel)
}