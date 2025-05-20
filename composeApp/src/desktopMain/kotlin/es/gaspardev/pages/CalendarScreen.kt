package es.gaspardev.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.LocalRouter
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.calendar.DayView
import es.gaspardev.layout.calendar.MonthView
import es.gaspardev.layout.calendar.WeekView
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Data classes
data class CalendarEvent(
    val id: String,
    val title: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val type: EventType,
    val athleteId: String?,
    val athleteName: String,
    val location: String,
    val notes: String
)

enum class EventType {
    WORKOUT, NUTRITION, ASSESSMENT, GROUP, OTHER
}

data class Athlete(
    val id: String,
    val name: String
)

// Sample data
val sampleEvents = listOf(
    CalendarEvent(
        id = "1",
        title = "Strength Training with Carlos",
        date = LocalDate.of(2025, 3, 18),
        startTime = LocalTime.of(10, 0),
        endTime = LocalTime.of(11, 0),
        type = EventType.WORKOUT,
        athleteId = "1",
        athleteName = "Carlos Rodriguez",
        location = "Main Gym",
        notes = "Focus on upper body strength"
    ),
)

val sampleAthletes = listOf(
    Athlete("1", "Carlos Rodriguez"),
    Athlete("2", "Maria Garcia"),
    Athlete("3", "Juan Lopez"),
    Athlete("4", "Ana Martinez")
)

@Composable
fun getEventTypeColor(type: EventType): Color {
    return when (type) {
        EventType.WORKOUT -> MaterialTheme.colors.primary
        EventType.NUTRITION -> Color(0xFF4CAF50)
        EventType.ASSESSMENT -> Color(0xFF2196F3)
        EventType.GROUP -> Color(0xFFFFC107)
        EventType.OTHER -> Color(0xFF9E9E9E)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarScreen() {

    val controler = LocalRouter.current

    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var searchQuery by remember { mutableStateOf("") }
    var viewType by remember { mutableStateOf("month") }
    var isModalOpen by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<CalendarEvent?>(null) }
    var filterType by remember { mutableStateOf("all") }

    val filteredEvents = sampleEvents.filter { event ->
        val matchesSearch = event.title.contains(searchQuery, ignoreCase = true) ||
                event.athleteName.contains(searchQuery, ignoreCase = true) ||
                event.location.contains(searchQuery, ignoreCase = true)

        if (filterType == "all") matchesSearch else matchesSearch && event.type.name.equals(
            filterType,
            ignoreCase = true
        )
    }

    fun handlePrevious() {
        currentDate = when (viewType) {
            "month" -> currentDate.minusMonths(1)
            "week" -> currentDate.minusWeeks(1)
            else -> currentDate.minusDays(1)
        }
    }

    fun handleNext() {
        currentDate = when (viewType) {
            "month" -> currentDate.plusMonths(1)
            "week" -> currentDate.plusWeeks(1)
            else -> currentDate.plusDays(1)
        }
    }

    fun handleToday() {
        currentDate = LocalDate.now()
    }

    fun openNewEventModal() {
        selectedEvent = null
        isModalOpen = true
    }

    fun openEditEventModal(event: CalendarEvent) {
        selectedEvent = event
        isModalOpen = true
    }

    // Month view generation
    fun generateMonthDays(): List<LocalDate> {
        val firstDayOfMonth = currentDate.withDayOfMonth(1)
        val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())

        val startDate = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek.value.toLong() % 7)
        val endDate = lastDayOfMonth.plusDays(6 - (lastDayOfMonth.dayOfWeek.value.toLong() % 7))

        val days = mutableListOf<LocalDate>()
        var day = startDate

        while (!day.isAfter(endDate)) {
            days.add(day)
            day = day.plusDays(1)
        }

        return days
    }

    // Week view generation
    fun generateWeekDays(): List<LocalDate> {
        val startDate = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() % 7)
        val endDate = startDate.plusDays(6)

        val days = mutableListOf<LocalDate>()
        var day = startDate

        while (!day.isAfter(endDate)) {
            days.add(day)
            day = day.plusDays(1)
        }

        return days
    }

    // Get events for a specific day
    fun getEventsForDay(day: LocalDate): List<CalendarEvent> {
        return sampleEvents.filter { it.date == day }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Calendar",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Schedule and manage your training sessions",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Button(
                        onClick = { openNewEventModal() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(Modifier.width(8.dp))
                        Text("New Event")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { handlePrevious() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
                        }
                        Button(
                            onClick = { handleToday() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.onSecondary
                            )
                        ) {
                            Text("Today")
                        }
                        IconButton(onClick = { handleNext() }) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
                        }
                        Text(
                            text = when (viewType) {
                                "month" -> currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                                "week" -> {
                                    val startDate = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() % 7)
                                    val endDate = startDate.plusDays(6)
                                    "${startDate.format(DateTimeFormatter.ofPattern("MMM d"))} - ${
                                        endDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                                    }"
                                }

                                else -> currentDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.width(200.dp),
                            placeholder = { Text("Search events...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = when (filterType) {
                                    "all" -> "All Events"
                                    "workout" -> "Workouts"
                                    "nutrition" -> "Nutrition"
                                    "assessment" -> "Assessments"
                                    "group" -> "Group Sessions"
                                    else -> "Filter by type"
                                },
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .width(150.dp),
                                shape = RoundedCornerShape(8.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        filterType = "all"
                                        expanded = false
                                    }
                                ) { Text("All Events") }
                                DropdownMenuItem(
                                    onClick = {
                                        filterType = "workout"
                                        expanded = false
                                    }
                                ) { Text("Workouts") }
                                DropdownMenuItem(
                                    onClick = {
                                        filterType = "nutrition"
                                        expanded = false
                                    }
                                ) { Text("Nutrition") }
                                DropdownMenuItem(
                                    onClick = {
                                        filterType = "assessment"
                                        expanded = false
                                    }
                                ) { Text("Assessments") }
                                DropdownMenuItem(
                                    onClick = {
                                        filterType = "group"
                                        expanded = false
                                    }
                                ) { Text("Group Sessions") }
                            }
                        }

                        TabRow(
                            selectedTabIndex = when (viewType) {
                                "month" -> 0
                                "week" -> 1
                                "day" -> 2
                                else -> 0
                            },
                            backgroundColor = Color.Transparent,
                            contentColor = MaterialTheme.colors.primary
                        ) {
                            listOf("Month", "Week", "Day").forEachIndexed { index, title ->
                                Tab(
                                    selected = when (index) {
                                        0 -> viewType == "month"
                                        1 -> viewType == "week"
                                        2 -> viewType == "day"
                                        else -> false
                                    },
                                    onClick = {
                                        viewType = when (index) {
                                            0 -> "month"
                                            1 -> "week"
                                            2 -> "day"
                                            else -> "month"
                                        }
                                    },
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                when (index) {
                                                    0 -> FitMeIcons.Calendar
                                                    1 -> Icons.Default.AccountBox
                                                    else -> FitMeIcons.ChartBar
                                                },
                                                contentDescription = title,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(title)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            when (viewType) {
                "month" -> MonthView(
                    currentDate = currentDate,
                    days = generateMonthDays(),
                    events = sampleEvents,
                    onEventClick = { openEditEventModal(it) }
                )

                "week" -> WeekView(
                    days = generateWeekDays(),
                    events = sampleEvents,
                    onEventClick = { openEditEventModal(it) }
                )

                "day" -> DayView(
                    date = currentDate,
                    events = filteredEvents,
                    onEventClick = { openEditEventModal(it) }
                )
            }
        }
    }

}
