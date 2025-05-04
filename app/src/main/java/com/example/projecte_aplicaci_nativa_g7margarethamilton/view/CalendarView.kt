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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Calendar_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.CalendarViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime
import java.util.*

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
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

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
                        text = localizedContext.getString(R.string.calendar_view_title),
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
                        Text(localizedContext.getString(R.string.calendar_create))
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
                            TaskItem(
                                task = task,
                                onUpdateTask = { updatedTask ->
                                    calendarViewModel.updateCalendarTask(
                                        taskId = updatedTask.id,
                                        title = updatedTask.title,
                                        content = updatedTask.content,
                                        isCompleted = updatedTask.is_completed,
                                        startTime = updatedTask.start_time,
                                        endTime = updatedTask.end_time,
                                        categoryId = updatedTask.id_category,
                                        email = currentUser?.email ?: ""
                                    )
                                },
                                showEditButton = true,
                                currentUser = currentUser?.email ?: "",
                                onDeleteTask = {
                                    calendarViewModel.deleteCalendarTask(task.id.toString())
                                }
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
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
            onAddTask = { showCreateEventDialog = true },
            onUpdateTask = { updatedTask ->
                calendarViewModel.updateCalendarTask(
                    taskId = updatedTask.id,
                    title = updatedTask.title,
                    content = updatedTask.content,
                    isCompleted = updatedTask.is_completed,
                    startTime = updatedTask.start_time,
                    endTime = updatedTask.end_time,
                    categoryId = updatedTask.id_category,
                    email = currentUser?.email ?: ""
                )
            }
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

@Composable
fun CreateCalendarDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var viewModel: UserViewModel = viewModel()

    val context = LocalContext.current
    val lang = viewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

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
                Text(localizedContext.getString(R.string.calendar_cancel))
            }
        }
    )
}

@Composable
fun TaskItem(
    task: Calendar_task,
    onUpdateTask: (Calendar_task) -> Unit = {},
    showEditButton: Boolean = true,
    currentUser: String = "",
    onDeleteTask: () -> Unit = {}
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var viewModel: UserViewModel = viewModel()

    val context = LocalContext.current
    val lang = viewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.is_completed) 
                MaterialTheme.colorScheme.surfaceVariant 
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.is_completed) 
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = try {
                            val startTime = ZonedDateTime.parse(task.start_time).toLocalTime()
                            val endTime = ZonedDateTime.parse(task.end_time).toLocalTime()
                            "${startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                        } catch (e: Exception) {
                            "${task.start_time.split(" ")[1]} - ${task.end_time.split(" ")[1]}"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (task.content.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Checkbox(
                    checked = task.is_completed,
                    onCheckedChange = { isChecked ->
                        onUpdateTask(task.copy(is_completed = isChecked))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline
                    )
                )
                if (showEditButton) {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = localizedContext.getString(R.string.calendar_edit_task),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDeleteTask) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = localizedContext.getString(R.string.calendar_delete_task),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        EditTaskDialog(
            task = task,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedTask ->
                onUpdateTask(updatedTask)
                showEditDialog = false
            },
            currentUser = currentUser
        )
    }
}

@Composable
fun EditTaskDialog(
    task: Calendar_task,
    onDismiss: () -> Unit,
    onConfirm: (Calendar_task) -> Unit,
    currentUser: String = ""
) {
    var title by remember { mutableStateOf(task.title) }
    var content by remember { mutableStateOf(task.content) }

    // Extraer fecha y horas de las strings existentes
    val initialDate = try {
        val datePart = task.start_time.split(" ")[0]
        LocalDate.parse(datePart)
    } catch (e: Exception) {
        LocalDate.now()
    }

    val initialStartTime = try {
        val timePart = task.start_time.split(" ")[1].split(":")
        Pair(timePart[0].toInt(), timePart[1].toInt())
    } catch (e: Exception) {
        Pair(LocalDateTime.now().hour, 0)
    }

    val initialEndTime = try {
        val timePart = task.end_time.split(" ")[1].split(":")
        Pair(timePart[0].toInt(), timePart[1].toInt())
    } catch (e: Exception) {
        Pair(LocalDateTime.now().hour + 1, 0)
    }

    var selectedDate by remember { mutableStateOf(initialDate) }
    var startHour by remember { mutableStateOf(initialStartTime.first) }
    var startMinute by remember { mutableStateOf(initialStartTime.second) }
    var endHour by remember { mutableStateOf(initialEndTime.first) }
    var endMinute by remember { mutableStateOf(initialEndTime.second) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var viewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    val lang = viewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    // Función simple para combinar fecha y hora en el formato requerido
    fun combineDateTime(date: LocalDate, hour: Int, minute: Int): String {
        val dateStr = date.format(DateTimeFormatter.ISO_DATE)
        val timeStr = String.format("%02d:%02d:00", hour, minute)
        return "$dateStr $timeStr"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Evento") },
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
                Text(localizedContext.getString(R.string.calendar_select_date), style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es"))))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
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
                        task.copy(
                            title = title,
                            content = content,
                            start_time = combineDateTime(selectedDate, startHour, startMinute),
                            end_time = combineDateTime(selectedDate, endHour, endMinute)
                        )
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text(localizedContext.getString(R.string.calendar_accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(localizedContext.getString(R.string.calendar_cancel))
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

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onConfirm = { date ->
                selectedDate = date
                showDatePicker = false
            },
            initialDate = selectedDate
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var userViewModel: UserViewModel = viewModel()
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    var isHourSelection by remember { mutableStateOf(true) }
    val hourScrollState = rememberScrollState()
    val minuteScrollState = rememberScrollState()
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)

    LaunchedEffect(isHourSelection) {
        // Scroll to center selected value when switching between hour/minute
        if (isHourSelection) {
            hourScrollState.scrollTo((selectedHour * 50).coerceAtMost(hourScrollState.maxValue))
        } else {
            minuteScrollState.scrollTo((selectedMinute * 50).coerceAtMost(minuteScrollState.maxValue))
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = localizedContext.getString(R.string.calendar_select_time),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Digital clock display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isHourSelection)
                                    MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .clickable { isHourSelection = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%02d", selectedHour),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isHourSelection)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (!isHourSelection)
                                    MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .clickable { isHourSelection = false }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%02d", selectedMinute),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (!isHourSelection)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Selector with two columns
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Hours column
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(hourScrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Add padding at top for better scrolling
                            Spacer(modifier = Modifier.height(80.dp))

                            for (hour in 0..23) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (hour == selectedHour && isHourSelection)
                                                MaterialTheme.colorScheme.primary
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            selectedHour = hour
                                            isHourSelection = true
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = String.format("%02d", hour),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = if (hour == selectedHour) FontWeight.Bold else FontWeight.Normal,
                                        color = if (hour == selectedHour && isHourSelection)
                                            MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Add padding at bottom for better scrolling
                            Spacer(modifier = Modifier.height(80.dp))
                        }

                        // Overlay gradients for scroll effect
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            Color.Transparent
                                        )
                                    )
                                )
                                .align(Alignment.TopCenter)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                                )
                                .align(Alignment.BottomCenter)
                        )

                        // Center indicator
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            thickness = 2.dp
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 25.dp)
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = -25.dp)
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )
                    }

                    // Vertical divider
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                    )

                    // Minutes column
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(minuteScrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Add padding at top for better scrolling
                            Spacer(modifier = Modifier.height(80.dp))

                            for (minute in 0..59) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (minute == selectedMinute && !isHourSelection)
                                                MaterialTheme.colorScheme.primary
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            selectedMinute = minute
                                            isHourSelection = false
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = String.format("%02d", minute),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = if (minute == selectedMinute) FontWeight.Bold else FontWeight.Normal,
                                        color = if (minute == selectedMinute && !isHourSelection)
                                            MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Add padding at bottom for better scrolling
                            Spacer(modifier = Modifier.height(80.dp))
                        }

                        // Overlay gradients for scroll effect
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            Color.Transparent
                                        )
                                    )
                                )
                                .align(Alignment.TopCenter)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                                )
                                .align(Alignment.BottomCenter)
                        )

                        // Center indicator
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            thickness = 2.dp
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 25.dp)
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = -25.dp)
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )
                    }
                }

                // Time period AM/PM indicator (optional)
                if (isHourSelection) {
                    Text(
                        text = localizedContext.getString(R.string.calendar_24h_format),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedHour, selectedMinute) },
                modifier = Modifier.padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(localizedContext.getString(R.string.calendar_accept))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(localizedContext.getString(R.string.calendar_cancel))
            }
        }
    )
}

fun generateDaysInMonth(yearMonth: YearMonth): List<LocalDate> {
    val firstOfMonth = yearMonth.atDay(1)
    val firstDayOfGrid = firstOfMonth.minusDays(firstOfMonth.dayOfWeek.value.toLong() - 1)
    return (0..41).map { firstDayOfGrid.plusDays(it.toLong()) }
        .filter { it.month == yearMonth.month }
}

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

@Composable
fun DayDetailDialog(
    date: LocalDate,
    tasks: List<Calendar_task>,
    onDismiss: () -> Unit,
    onAddTask: () -> Unit,
    onUpdateTask: (Calendar_task) -> Unit
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
                        TaskItem(
                            task = task,
                            onUpdateTask = onUpdateTask,
                            showEditButton = true,
                            currentUser = "",
                            onDeleteTask = {
                                onUpdateTask(task.copy(is_completed = true))
                                onDismiss()
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
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

@Composable
fun CreateEventDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(initialDate) }
    var startHour by remember { mutableStateOf(LocalDateTime.now().hour) }
    var startMinute by remember { mutableStateOf(0) }
    var endHour by remember { mutableStateOf(LocalDateTime.now().hour) }
    var endMinute by remember { mutableStateOf(0) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Función para formatear la fecha y hora al formato requerido
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    initialDate: LocalDate = LocalDate.now()
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentYearMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)


    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = localizedContext.getString(R.string.calendar_select_date),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { 
                        currentYearMonth = currentYearMonth.minusMonths(1)
                    }) {
                        Icon(Icons.Default.KeyboardArrowLeft, localizedContext.getString(R.string.calendar_previous_month))
                    }
                    Text(
                        text = currentYearMonth.format(
                            DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = { 
                        currentYearMonth = currentYearMonth.plusMonths(1)
                    }) {
                        Icon(Icons.Default.KeyboardArrowRight, localizedContext.getString(R.string.calendar_next_month))
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Días de la semana
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val weekdays = localizedContext.getString(R.string.calendar_weekdays).split(",")
                    weekdays.forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.padding(4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Cuadrícula de días
                val days = generateDaysInMonth(currentYearMonth)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(240.dp)
                ) {
                    items(days) { date ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    when {
                                        date == selectedDate -> MaterialTheme.colorScheme.primary
                                        date == LocalDate.now() -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                        date.month != currentYearMonth.month -> Color.Transparent
                                        else -> Color.Transparent
                                    },
                                    shape = CircleShape
                                )
                                .clickable(
                                    enabled = date.month == currentYearMonth.month,
                                    onClick = { selectedDate = date }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = when {
                                    date == selectedDate -> MaterialTheme.colorScheme.onPrimary
                                    date.month != currentYearMonth.month -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedDate) }
            ) {
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarViewPreview() {
    //val navController = rememberNavController()
    // Nota: Esto no funcionará en la preview debido a la dependencia del ViewModel
    // CalendarView(navController)
}
