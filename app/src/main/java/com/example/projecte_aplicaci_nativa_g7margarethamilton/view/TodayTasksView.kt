package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.CalendarViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Vista que muestra las tareas del día actual
 * @param userViewModel ViewModel del usuario para gestionar la sesión
 * @param calendarViewModel ViewModel del calendario para gestionar las tareas
 * @param navController Controlador de navegación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayTasksView(
    userViewModel: UserViewModel,
    calendarViewModel: CalendarViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val lang = userViewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    
    // Obtener las tareas del calendario
    val calendarTasks by calendarViewModel.calendarTasks.collectAsState()
    val today = LocalDate.now()

    // Filtrar las tareas para hoy
    val todayTasks = calendarTasks.filter { task ->
        val taskDate = try {
            ZonedDateTime.parse(task.start_time).toLocalDate()
        } catch (e: Exception) {
            LocalDate.parse(task.start_time.split(" ")[0])
        }
        taskDate == today
    }.sortedBy { it.start_time }

    // Cargar las tareas al iniciar la vista
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
                        text = localizedContext.getString(R.string.today_tasks_title),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                .padding(16.dp)
        ) {
            // Encabezado con la fecha actual
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = today.format(DateTimeFormatter.ofPattern("EEEE", Locale(lang)))
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = today.format(DateTimeFormatter.ofPattern(localizedContext.getString(R.string.calendar_date_pattern), Locale(lang))),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(onClick = {navController.navigate(Routes.Calendar.route)}) // Navegar al calendario})
                    )
                }
            }

            // Resumen de tareas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TaskSummaryCard(
                    title = "Total",
                    count = todayTasks.size,
                    color = MaterialTheme.colorScheme.primary
                )
                TaskSummaryCard(
                    title = localizedContext.getString(R.string.today_tasks_completed),
                    count = todayTasks.count { it.is_completed },
                    color = MaterialTheme.colorScheme.tertiary
                )
                TaskSummaryCard(
                    title = localizedContext.getString(R.string.today_tasks_pending),
                    count = todayTasks.count { !it.is_completed },
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Lista de tareas
            if (todayTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks for today",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todayTasks) { task ->
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
                                    email = userViewModel.currentUser.value?.email ?: ""
                                )
                            },
                            onDeleteTask = {
                                calendarViewModel.deleteCalendarTask(task.id.toString())
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskSummaryCard(
    title: String,
    count: Int,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}