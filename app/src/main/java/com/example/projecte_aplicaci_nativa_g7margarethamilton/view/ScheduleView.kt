package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.ScheduleViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import kotlinx.coroutines.flow.filter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleView(
    navController: NavController,
    userViewModel: UserViewModel,
    viewModel: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
        ScheduleViewModel(userViewModel)
    }
) {
    val currentDate = remember { mutableStateOf(LocalDate.now()) }
    val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("es"))
    val weekFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("es"))

    val schedules by viewModel.schedules.collectAsState()
    val currentSchedule by viewModel.currentSchedule.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()
    val currentTasks by viewModel.currentScheduleTasks.collectAsState()
    val filteredTasks by viewModel.filteredTasksByDay.collectAsState()
    val showAddTaskDialog = remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val week_day = remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_WEEK)) }
    var diaSemanaString = when (week_day.intValue) {
        1 -> "Domingo"
        2 -> "Lunes"
        3 -> "Martes"
        4 -> "Miércoles"
        5 -> "Jueves"
        6 -> "Viernes"
        7 -> "Sábado"
        else -> ""
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())



    // Filtrar tareas cuando cambia el día de la semana
    LaunchedEffect(week_day.intValue) {
        viewModel.filterTasksByDay(week_day.intValue)
    }

    //añadirDatosPrueba(viewModel, userViewModel)

    LaunchedEffect(currentUser) {
        currentUser?.email?.let { email ->
            Log.d("ScheduleView", "Loading schedules for user: $email")
            viewModel.loadSchedules(email)
        } ?: Log.d("ScheduleView", "Cannot load schedules: user email is null")
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            //.padding(40.dp),

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Agenda",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    // Close button
                    IconButton(
                        onClick = { navController.navigate(Routes.Home.route) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    // Calendar button
                    IconButton(
                        onClick = { /* navController.navigate(Routes.Calendar.route)*/ },
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendario",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .padding(top = 40.dp)
                .padding(paddingValues)
        ) {
            // Dropdown para seleccionar la agenda actual
            ScheduleDropdown(
                schedules = schedules,
                currentSchedule = currentSchedule,
                onScheduleSelected = { selectedSchedule ->
                    viewModel.setCurrentSchedule(selectedSchedule)
                }
            )

            // Navegación de días
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        //currentDate.value = currentDate.value.minusDays(1)
                        week_day.intValue = if (week_day.intValue == 1) 7 else week_day.intValue - 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Día anterior"
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = diaSemanaString,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                IconButton(
                    onClick = {
                        //currentDate.value = currentDate.value.plusDays(1)
                        week_day.intValue = if (week_day.intValue == 7) 1 else week_day.intValue + 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Día siguiente"
                    )
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
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                if (filteredTasks.isEmpty()) {
                    Text(
                        text = "No hay tareas programadas para este día.",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    filteredTasks.forEach { task ->
                        TaskItem(task)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
//                if (currentTasks.isEmpty()) {
//                    Text(
//                        text = "No hay tareas programadas.",
//                        fontSize = 16.sp,
//                        color = MaterialTheme.colorScheme.onSecondary,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                } else{
//                    Text(
//                        text = "Mostrando ${currentTasks.size} tareas (${filteredTasks.size} para día actual)",
//                        fontSize = 16.sp,
//                        color = MaterialTheme.colorScheme.onSecondary,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                    currentTasks.forEach { task ->
//                        Text(
//                            text = "Tarea: ${task.title} - Día: ${task.week_day}",
//                            fontSize = 14.sp,
//                            color = MaterialTheme.colorScheme.onSecondary,
//                            modifier = Modifier.padding(8.dp)
//                        )
//                        TaskItem(task)
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }
//                }
            }

            // Botón para añadir tarea
            Button(
                onClick = { showAddTaskDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Añadir Tarea")
            }
        }
    }

    // Dialog para añadir tarea
    if (showAddTaskDialog.value) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog.value = false },
            onConfirm = { title, content, startTime, endTime, week_day, categoryId ->
                currentUser?.email?.let { email ->
                    currentSchedule?.id?.let { scheduleId ->
                        viewModel.addTaskToSchedule(
                            scheduleId = scheduleId.toString(),
                            title = title,
                            content = content,
                            startTime = startTime,
                            endTime = endTime,
                            week_day = week_day,
                            categoryId = categoryId,
                            email = email
                        )
                    }
                }
                showAddTaskDialog.value = false
            }
        )
    }
}

//fun añadirDatosPrueba(
//    viewModel: ScheduleViewModel,
//    userViewModel: UserViewModel
//): List<Schedule_task> {
//    val email = userViewModel.currentUser.value?.email.toString()
//    viewModel.createNewSchedule(
//        "Agenda de prueba",
//        email,
//        1
//    )
//
//    // Esperar un momento para que se cree la agenda
//    Thread.sleep(1000)
//
//    val scheduleId = viewModel.schedules.value.firstOrNull { it.email == email }?.id.toString()
//
//    if (scheduleId.isNotEmpty()) {
//        viewModel.addTaskToSchedule(
//            scheduleId,
//            "Tarea de prueba 1",
//            "Contenido de la tarea de prueba 1",
//            "08:00",
//            "09:00",
//            1,
//            1,
//            email
//        )
//        viewModel.addTaskToSchedule(
//            scheduleId,
//            "Tarea de prueba 2",
//            "Contenido de la tarea de prueba 2",
//            "10:00",
//            "11:00",
//            2,
//            1,
//            email
//        )
//    }
//
//    return viewModel.schedules.value.firstOrNull { it.id.toString() == scheduleId }?.tasks
//        ?: emptyList()
//}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(task: Schedule_task) {
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
                text = task.start_time.substring(0, 5),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .height(1.dp),
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = task.end_time.substring(0, 5),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Color.LightGray)
                .background(Color.White)
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
                    text = task.content,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondary
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
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ScheduleDropdown(
    schedules: List<Schedule>,
    currentSchedule: Schedule?,
    onScheduleSelected: (Schedule) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = currentSchedule?.title ?: "Selecciona una agenda"

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                selectedText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown Arrow"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            schedules.forEach { schedule ->
                DropdownMenuItem(
                    text = { Text(schedule.title) },
                    onClick = {
                        expanded = false
                        onScheduleSelected(schedule)
                    }
                )
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//fun formatDateHumanReadable(dateStr: String): String {
//    val dateTime = OffsetDateTime.parse(dateStr)
//    val formatter = DateTimeFormatter.ofPattern("HH'/'MM")
//    return dateTime.format(formatter)
//}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskPreview() {

    val navController = rememberNavController()
    val userViewModel = androidx.lifecycle.viewmodel.compose.viewModel<UserViewModel>()
    ScheduleView(navController, userViewModel)
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Int, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf(1) }
    val calendar = Calendar.getInstance()
    // Calendar.DAY_OF_WEEK comienza con domingo=1, lunes=2, etc.
    // Para obtener 0-6 (donde 0 es domingo), resta 1
    val week_day = calendar.get(Calendar.DAY_OF_WEEK)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenido") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Hora de inicio (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("Hora de fin (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(title, content, startTime, endTime, week_day, categoryId)
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

