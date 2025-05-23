package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.ScheduleViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleView(
    navController: NavController,
    userViewModel: UserViewModel,
    viewModel: ScheduleViewModel = viewModel {
        ScheduleViewModel(userViewModel)
    }
) {

    val schedules by viewModel.schedules.collectAsState()
    val currentSchedule by viewModel.currentSchedule.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()
    val filteredTasks by viewModel.filteredTasksByDay.collectAsState()
    val showAddTaskDialog = remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
    val weekDay = remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_WEEK)) }
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)
    val diaSemanaString = when (weekDay.intValue) {
        1 -> localizedContext.getString(R.string.common_sunday)
        2 -> localizedContext.getString(R.string.common_monday)
        3 -> localizedContext.getString(R.string.common_tuesday)
        4 -> localizedContext.getString(R.string.common_wednesday)
        5 -> localizedContext.getString(R.string.common_thursday)
        6 -> localizedContext.getString(R.string.common_friday)
        7 -> localizedContext.getString(R.string.common_saturday)
        else -> ""
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Filtrar tareas cuando cambia el día de la semana
    LaunchedEffect(weekDay.intValue) {
        viewModel.filterTasksByDay(weekDay.intValue)
    }

    //añadirDatosPrueba(viewModel, userViewModel)
    viewModel.filterTasksByDay(weekDay.intValue)

    LaunchedEffect(currentUser) {
        currentUser?.email?.let { email ->
            Log.d("ScheduleView", "Loading schedules for user: $email")
            viewModel.loadSchedules()
        } ?: Log.d("ScheduleView", "Cannot load schedules: user email is null")
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(top = 40.dp),                // mateix padding que Calendar
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = localizedContext.getString(R.string.schedule_view_title),
                        fontSize = 30.sp,        // mateix fontSize que Calendar
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,   // igual que Calendar
                            contentDescription = localizedContext.getString(R.string.schedule_close),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Routes.Calendar.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = localizedContext.getString(R.string.schedule_calendar),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(   // mateix esquema de colors
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                scrollBehavior = scrollBehavior               // idèntic valor
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
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
                },
                onCreateSchedule = { title, categoryId ->
                    currentUser?.email?.let { email ->
                        viewModel.createNewSchedule(title, email, categoryId)
                    }
                },
                onDelete = {
                    currentSchedule?.id?.let { scheduleId ->
                        viewModel.deleteSchedule(scheduleId.toString())
                    }
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
                        weekDay.intValue = if (weekDay.intValue == 1) 7 else weekDay.intValue - 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Día anterior"
                    )
                }

                Column(
                    modifier = Modifier
                        .clickable {
                            weekDay.intValue = currentDay
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = diaSemanaString,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    if (currentDay == weekDay.intValue) {
                        Text(
                            text = localizedContext.getString(R.string.common_today),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Thin,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                IconButton(
                    onClick = {
                        weekDay.intValue = if (weekDay.intValue == 7) 1 else weekDay.intValue + 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
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
                        text = localizedContext.getString(R.string.schedule_no_tasks),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    filteredTasks.forEach { task ->
                        TaskItem(
                            task = task,
                            onDelete = {
                                viewModel.deleteScheduleTask(task.id.toString())
                                // Recargar tareas tras crear una
                                viewModel.filterTasksByDay(weekDay.intValue)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }

            // Botón para añadir tarea
            Button(
                onClick = { showAddTaskDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = currentSchedule != null,
            ) {
                Text(localizedContext.getString(R.string.schedule_add_task))
            }
        }
    }

    // Dialog para añadir tarea
    if (showAddTaskDialog.value) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog.value = false },
            onConfirm = { title, content, startTime, endTime, categoryId ->
                currentUser?.email?.let { email ->
                    currentSchedule?.id?.let { scheduleId ->
                        viewModel.addTaskToSchedule(
                            scheduleId = scheduleId.toString(),
                            title = title,
                            content = content,
                            startTime = startTime,
                            endTime = endTime,
                            week_day = weekDay.intValue,
                            categoryId = categoryId,
                            email = email
                        )
                        // Recargar tareas tras crear una
                        viewModel.filterTasksByDay(weekDay.intValue)
                    }
                }
                showAddTaskDialog.value = false
            }
        )
    }
}


@Composable
fun TaskItem(task: Schedule_task, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tiempo
            Column(
                modifier = Modifier
                    .width(70.dp)
                    .padding(end = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = task.start_time.substring(0, 5),
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.end_time.substring(0, 5),
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }

            // Línea vertical decorativa
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            // Contenido
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold
                )

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Text(
                        text = task.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Botón de eliminar con animación
            IconButton(
                onClick = {
                    // Animación de desvanecimiento antes de eliminar
                    expanded = false
                    onDelete()
                },
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ScheduleDropdown(
    schedules: List<Schedule>,
    currentSchedule: Schedule?,
    onScheduleSelected: (Schedule) -> Unit,
    onCreateSchedule: (String, Int) -> Unit,
    onDelete: () -> Unit
) {
    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    var expanded by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }
    val selectedText = currentSchedule?.title ?: localizedContext.getString(R.string.schedule_no_agenda)



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
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
            // Opción para crear nueva agenda
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear nueva agenda",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            localizedContext.getString(R.string.schedule_new_agenda),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                onClick = {
                    expanded = false
                    showCreateDialog = true
                }
            )

            // Separador
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            // Lista de agendas existentes
            schedules.forEach { schedule ->

                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(schedule.title)
                            IconButton(
                                onClick = {
                                    expanded = false
                                    onDelete()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar agenda",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    },
                    onClick = {
                        expanded = false
                        onScheduleSelected(schedule)
                    }
                )


            }
        }

        if (showCreateDialog) {
            CreateScheduleDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { title, categoryId ->
                    onCreateSchedule(title, categoryId)
                    showCreateDialog = false
                }
            )
        }
    }
}


@Composable
fun CreateScheduleDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    val categoryId by remember { mutableStateOf(1) } // Valor por defecto
    var titleError by remember { mutableStateOf(false) }

    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                localizedContext.getString(R.string.schedule_new_agenda),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = it.isBlank()
                    },
                    label = { Text(localizedContext.getString(R.string.common_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = titleError,
                    supportingText = {
                        if (titleError) {
                            Text(
                                text = localizedContext.getString(R.string.schedule_title_empty),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                // Aquí podrías añadir más campos si son necesarios
                // Por ejemplo, un selector de categoría
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, categoryId)
                    } else {
                        titleError = true
                    }
                }
            ) {
                Text(localizedContext.getString(R.string.schedule_create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(localizedContext.getString(R.string.calendar_cancel))
            }
        }
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskPreview() {

    val navController = rememberNavController()
    val userViewModel = viewModel<UserViewModel>()
    ScheduleView(navController, userViewModel)
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Int) -> Unit
) {
    // Estado para campos del formulario
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val categoryId by remember { mutableStateOf(1) }

    // Estado para validación de errores
    var titleError by remember { mutableStateOf(false) }
    var contentError by remember { mutableStateOf(false) }
    var startTimeError by remember { mutableStateOf(false) }
    var endTimeError by remember { mutableStateOf(false) }

    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    // Regex para validar formato de hora
    val timeFormatRegex = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")

    // Función para validar todos los campos
    fun validateInputs(): Boolean {
        titleError = title.isBlank()
        contentError = content.isBlank()
        startTimeError = startTime.isBlank() || !startTime.matches(timeFormatRegex)
        endTimeError = endTime.isBlank() || !endTime.matches(timeFormatRegex)

        return !titleError && !contentError && !startTimeError && !endTimeError
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(localizedContext.getString(R.string.schedule_new_task)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Campo de título
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = it.isBlank()
                    },
                    label = { Text(localizedContext.getString(R.string.common_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = titleError,
                    supportingText = {
                        if (titleError) {
                            Text(
                                text = localizedContext.getString(R.string.schedule_title_empty),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                // Campo de contenido
                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        content = it
                        contentError = it.isBlank()
                    },
                    label = { Text(localizedContext.getString(R.string.common_description)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = contentError,
                    supportingText = {
                        if (contentError) {
                            Text(
                                text = localizedContext.getString(R.string.common_empty_field),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                // Campo de hora de inicio
                OutlinedTextField(
                    value = startTime,
                    onValueChange = {
                        startTime = it
                        startTimeError = it.isBlank() || !it.matches(timeFormatRegex)
                    },
                    label = { Text("${localizedContext.getString(R.string.common_start_hour)} (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = startTimeError,
                    supportingText = {
                        if (startTimeError) {
                            Text(
                                text = "${localizedContext.getString(R.string.common_invalid_format)} (HH:mm)",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                // Campo de hora de fin
                OutlinedTextField(
                    value = endTime,
                    onValueChange = {
                        endTime = it
                        endTimeError = it.isBlank() || !it.matches(timeFormatRegex)
                    },
                    label = { Text("${localizedContext.getString(R.string.common_end_hour)} (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = endTimeError,
                    supportingText = {
                        if (endTimeError) {
                            Text(
                                text = "${localizedContext.getString(R.string.common_invalid_format)} (HH:mm)",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (validateInputs()) {
                        onConfirm(title, content, startTime, endTime, categoryId)
                    }
                }
            ) {
                Text(localizedContext.getString(R.string.schedule_add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(localizedContext.getString(R.string.calendar_cancel))
            }
        }
    )
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun TaskItemPreview() {
//    val sampleTask = Schedule_task(
//        id = 1,
//        title = "Reunión de equipo",
//        content = "Discutir los avances del proyecto y planificar próximos pasos",
//        priority = 1,
//        start_time = "09:00",
//        end_time = "10:30",
//        week_day = 2,
//        id_schedule = 1,
//        id_category = 1
//    )
//
//    TaskItem(
//        task = sampleTask,
//        onDelete = {}
//    )
//}

