package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
//import androidx.compose.material.icons.filled.ArrowLeft
//import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Calendar_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.CalendarViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarView(
    navController: NavController,
    calendarViewModel: CalendarViewModel,
    userViewModel: UserViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    val calendars by calendarViewModel.calendars.collectAsState()
    val calendarTasks by calendarViewModel.calendarTasks.collectAsState()
    var showCreateCalendarDialog by remember { mutableStateOf(false) }
    var showCreateEventDialog by remember { mutableStateOf(false) }
    var showDayDetail by remember { mutableStateOf(false) }
    val currentUser by userViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        calendarViewModel.loadCalendars()
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(top = 40.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Calendario",
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
        ) {
            // Navegación del mes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(Icons.Default.KeyboardArrowLeft, "Mes anterior")
                }
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.clickable { currentMonth = YearMonth.now() }
                )
                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(Icons.Default.KeyboardArrowRight, "Mes siguiente")
                }
            }

            // Días de la semana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val daysOfWeek = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Cuadrícula del calendario
            if (calendars.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { showCreateCalendarDialog = true }) {
                        Text("Crear Calendario")
                    }
                }
            } else {
                val daysInMonth = generateDaysInMonth(currentMonth)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.weight(0.5f)
                ) {
                    items(daysInMonth) { date ->
                        DayCell(
                            date = date,
                            isSelected = date == selectedDate,
                            hasEvents = calendarTasks.any { task ->
                                val taskDate = parseTaskDate(task.start_time)
                                taskDate == date
                            },
                            onClick = {
                                calendarViewModel.setSelectedDate(date)
                                showDayDetail = true
                            }
                        )
                    }
                }
            }

            // Lista de tareas agrupadas por día
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                val tasksByDate = calendarTasks.groupBy { task ->
                    parseTaskDate(task.start_time)
                }.toSortedMap()

                tasksByDate.forEach { (date, tasks) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es"))),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        tasks.forEach { task ->
                            TaskItem(task = task)
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }

    // Diálogo para crear calendario
    if (showCreateCalendarDialog) {
        CreateCalendarDialog(
            onDismiss = { showCreateCalendarDialog = false },
            onConfirm = { title ->
                calendarViewModel.createNewCalendar(
                    title = title,
                    email = currentUser?.email ?: "",
                    categoryId = 1
                )
                showCreateCalendarDialog = false
            }
        )
    }

    // Vista de detalle del día
    if (showDayDetail) {
        DayDetailDialog(
            date = selectedDate,
            tasks = calendarTasks.filter { task ->
                val taskDate = parseTaskDate(task.start_time)
                taskDate == selectedDate
            },
            onDismiss = { showDayDetail = false },
            onAddTask = { showCreateEventDialog = true }
        )
    }

    // Diálogo para crear evento
    if (showCreateEventDialog) {
        CreateEventDialog(
            initialDate = selectedDate,
            onDismiss = { showCreateEventDialog = false },
            onConfirm = { title, content, startTime, endTime ->
                calendarViewModel.createCalendarTask(
                    title = title,
                    content = content,
                    startTime = startTime,
                    endTime = endTime,
                    categoryId = 1,
                    email = currentUser?.email ?: ""
                )
                showCreateEventDialog = false
                showDayDetail = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    hasEvents: Boolean,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()
    
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .background(
                when {
                    isToday -> MaterialTheme.colorScheme.primary
                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }
            )
            .then(
                if (hasEvents) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isToday) MaterialTheme.colorScheme.onPrimary 
                   else MaterialTheme.colorScheme.onBackground
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateCalendarDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Calendario") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título del calendario") }
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title) },
                enabled = title.isNotBlank()
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(task: Calendar_task) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = try {
                    val startTime = ZonedDateTime.parse(task.start_time).toLocalTime()
                    val endTime = ZonedDateTime.parse(task.end_time).toLocalTime()
                    "${startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                } catch (e: Exception) {
                    "${task.start_time.split(" ")[1]} - ${task.end_time.split(" ")[1]}"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        if (task.is_completed) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completada",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayDetailDialog(
    date: LocalDate,
    tasks: List<Calendar_task>,
    onDismiss: () -> Unit,
    onAddTask: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = date.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es")))
            )
        },
        text = {
            Column {
                if (tasks.isEmpty()) {
                    Text("No hay tareas para este día")
                } else {
                    tasks.forEach { task ->
                        TaskItem(task = task)
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onAddTask) {
                Text("Añadir tarea")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateEventDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(initialDate) }
    var startHour by remember { mutableStateOf(0) }
    var startMinute by remember { mutableStateOf(0) }
    var endHour by remember { mutableStateOf(0) }
    var endMinute by remember { mutableStateOf(0) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    fun formatDateTime(date: LocalDate, hour: Int, minute: Int): String {
        return "${date.format(DateTimeFormatter.ISO_DATE)} ${String.format("%02d:%02d:00", hour, minute)}"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Evento") },
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
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Selector de fecha
                Text("Fecha", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es"))),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                // Selector de hora de inicio
                Text("Hora de inicio", style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(String.format("%02d:%02d", startHour, startMinute))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Selector de hora de fin
                Text("Hora de fin", style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(String.format("%02d:%02d", endHour, endMinute))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        title,
                        content,
                        formatDateTime(selectedDate, startHour, startMinute),
                        formatDateTime(selectedDate, endHour, endMinute)
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )

    if (showStartTimePicker) {
        TimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { hour, minute ->
                startHour = hour
                startMinute = minute
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { hour, minute ->
                endHour = hour
                endMinute = minute
                showEndTimePicker = false
            }
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar hora") },
        text = {
            Column {
                // Selector de hora
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Horas
                    NumberPicker(
                        value = selectedHour,
                        onValueChange = { selectedHour = it },
                        range = 0..23
                    )
                    Text(":", modifier = Modifier.padding(horizontal = 8.dp))
                    // Minutos
                    NumberPicker(
                        value = selectedMinute,
                        onValueChange = { selectedMinute = it },
                        range = 0..59
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedHour, selectedMinute) }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Column {
        IconButton(onClick = { 
            if (value < range.last) onValueChange(value + 1) 
        }) {
            Icon(Icons.Default.KeyboardArrowUp, "Incrementar")
        }
        Text(
            text = String.format("%02d", value),
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = { 
            if (value > range.first) onValueChange(value - 1) 
        }) {
            Icon(Icons.Default.KeyboardArrowDown, "Decrementar")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateDaysInMonth(yearMonth: YearMonth): List<LocalDate> {
    val firstOfMonth = yearMonth.atDay(1)
    val firstDayOfGrid = firstOfMonth.minusDays(firstOfMonth.dayOfWeek.value.toLong() - 1)
    return (0..41).map { firstDayOfGrid.plusDays(it.toLong()) }
        .filter { it.month == yearMonth.month }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseTaskDate(dateTimeStr: String): LocalDate {
    return try {
        // Intenta parsear primero como ZonedDateTime
        ZonedDateTime.parse(dateTimeStr).toLocalDate()
    } catch (e: Exception) {
        try {
            // Si falla, intenta parsear como LocalDate directamente
            LocalDate.parse(dateTimeStr.split(" ")[0])
        } catch (e: Exception) {
            // Si todo falla, devuelve la fecha actual
            LocalDate.now()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarViewPreview() {
    val navController = rememberNavController()
    // Nota: Esto no funcionará en la preview debido a la dependencia del ViewModel
    // CalendarView(navController)
}
