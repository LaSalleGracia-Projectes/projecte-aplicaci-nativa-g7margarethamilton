package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.CalendarViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
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
                    style = MaterialTheme.typography.titleLarge
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
                    modifier = Modifier.fillMaxSize(),
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
                    modifier = Modifier.weight(1f)
                ) {
                    items(daysInMonth) { date ->
                        DayCell(
                            date = date,
                            isSelected = date == selectedDate,
                            hasEvents = calendarTasks.any { task ->
                                // Aquí deberías implementar la lógica para verificar si hay eventos en este día
                                true // Por ahora siempre true
                            },
                            onClick = {
                                calendarViewModel.setSelectedDate(date)
                                showCreateEventDialog = true
                            }
                        )
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
                    categoryId = 1 // Categoría por defecto
                )
                showCreateCalendarDialog = false
            }
        )
    }

    // Diálogo para crear evento
    if (showCreateEventDialog) {
        CreateEventDialog(
            onDismiss = { showCreateEventDialog = false },
            onConfirm = { title, content, startTime, endTime ->
                calendarViewModel.createCalendarTask(
                    title = title,
                    content = content,
                    startTime = startTime,
                    endTime = endTime,
                    categoryId = 1, // Categoría por defecto
                    email = currentUser?.email ?: ""
                )
                showCreateEventDialog = false
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
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
            if (hasEvents) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            MaterialTheme.colorScheme.primary)
                        .clip(CircleShape)
                )
            }
        }
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
fun CreateEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, java.time.LocalTime, java.time.LocalTime) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf(java.time.LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(java.time.LocalTime.of(10, 0)) }

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
                // Aquí podrías agregar selectores de tiempo para startTime y endTime
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, content, startTime, endTime) },
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
fun generateDaysInMonth(yearMonth: YearMonth): List<LocalDate> {
    val firstOfMonth = yearMonth.atDay(1)
    val firstDayOfGrid = firstOfMonth.minusDays(firstOfMonth.dayOfWeek.value.toLong() - 1)
    return (0..41).map { firstDayOfGrid.plusDays(it.toLong()) }
        .filter { it.month == yearMonth.month }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarViewPreview() {
    val navController = rememberNavController()
    // Nota: Esto no funcionará en la preview debido a la dependencia del ViewModel
    // CalendarView(navController)
}
