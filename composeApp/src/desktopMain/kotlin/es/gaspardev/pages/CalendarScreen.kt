package es.gaspardev.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.comunication.Session
import es.gaspardev.enums.SessionType
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.calendar.DayView
import es.gaspardev.layout.calendar.MonthView
import es.gaspardev.layout.calendar.WeekView
import es.gaspardev.states.SessionState
import kotlinx.datetime.*

fun isLeapYear(year: Int): Boolean = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

@Composable
fun getEventTypeColor(type: SessionType): Color {
    return when (type) {
        SessionType.WORKOUT -> MaterialTheme.colors.primary
        SessionType.NUTRITION -> Color(0xFF4CAF50)
        SessionType.ASSESSMENT -> Color(0xFF2196F3)
        SessionType.GROUP -> Color(0xFFFFC107)
        SessionType.OTHER -> Color(0xFF9E9E9E)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarScreen() {

    val controler = LocalRouter.current

    val timeZone = TimeZone.currentSystemDefault()
    var currentDate by remember { mutableStateOf(Clock.System.now()) }
    var searchQuery by remember { mutableStateOf("") }
    var viewType by remember { mutableStateOf("month") }
    var isModalOpen by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Session?>(null) }
    var filterType by remember { mutableStateOf("all") }

    val filteredEvents = SessionState.state.filter { event ->
        val matchesSearch = event.title.contains(searchQuery, ignoreCase = true) ||
                event.with.contains(searchQuery, ignoreCase = true)

        if (filterType == "all") matchesSearch else matchesSearch && event.sessionType.name.equals(
            filterType,
            ignoreCase = true
        )
    }

    fun handlePrevious() {
        currentDate = when (viewType) {
            "month" -> currentDate.minus(1, DateTimeUnit.MONTH, timeZone)
            "week" -> currentDate.minus(1, DateTimeUnit.WEEK, timeZone)
            else -> currentDate.minus(1, DateTimeUnit.DAY, timeZone)
        }
    }

    fun handleNext() {
        currentDate = when (viewType) {
            "month" -> currentDate.plus(1, DateTimeUnit.MONTH, timeZone)
            "week" -> currentDate.plus(1, DateTimeUnit.WEEK, timeZone)
            else -> currentDate.plus(1, DateTimeUnit.DAY, timeZone)
        }
    }

    fun handleToday() {
        currentDate = Clock.System.now()
    }


    fun openNewEventModal() {
        selectedEvent = null
        isModalOpen = true
    }

    fun openEditEventModal(event: Session) {
        selectedEvent = event
        isModalOpen = true
    }

    fun generateMonthDays(): List<Instant> {
        val localDate = currentDate.toLocalDateTime(timeZone).date
        val firstDayOfMonth = LocalDate(localDate.year, localDate.month, 1)
        val lastDayOfMonth = when (localDate.month) {
            Month.FEBRUARY -> if (isLeapYear(localDate.year))
                LocalDate(localDate.year, localDate.month, 29)
            else LocalDate(localDate.year, localDate.month, 28)

            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER ->
                LocalDate(localDate.year, localDate.month, 30)

            else -> LocalDate(localDate.year, localDate.month, 31)
        }

        // Calculate the start date (first day of the week containing the first day of month)
        val startDate = firstDayOfMonth.minus(
            DatePeriod(days = (firstDayOfMonth.dayOfWeek.isoDayNumber - 1) % 7)
        )

        // Calculate the end date (last day of the week containing the last day of month)
        val endDate = lastDayOfMonth.plus(
            DatePeriod(days = (7 - lastDayOfMonth.dayOfWeek.isoDayNumber) % 7)
        )

        val days = mutableListOf<Instant>()
        var day = startDate

        while (day <= endDate) {
            days.add(day.atStartOfDayIn(timeZone))
            day = day.plus(1, DateTimeUnit.DAY)
        }

        return days
    }

    fun generateWeekDays(): List<Instant> {
        val localDate = currentDate.toLocalDateTime(timeZone).date
        val startDate = localDate.minus(DatePeriod(days = (localDate.dayOfWeek.isoDayNumber - 1) % 7))
        val endDate = startDate.plus(DatePeriod(days = 6))

        val days = mutableListOf<Instant>()
        var day = startDate

        while (day <= endDate) {
            days.add(day.atStartOfDayIn(timeZone))
            day = day.plus(1, DateTimeUnit.DAY)
        }

        return days
    }


    // Get events for a specific day
    fun getEventsForDay(day: Instant): List<Session> {
        val end = day.plus(1, DateTimeUnit.DAY, timeZone)
        return SessionState.state.filter {
            it.dateTime in day..<end
        }
    }


    // Scaffold removido para unificación visual
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card {
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
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Schedule and manage your training sessions",
                                    style = MaterialTheme.typography.body1
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
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.width(200.dp)
                                ) {
                                    Row {
                                        IconButton(onClick = { handlePrevious() }) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                                contentDescription = "Previous"
                                            )
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
                                            Icon(
                                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = "Next"
                                            )
                                        }
                                    }
                                    Text(
                                        text = when (viewType) {
                                            "month" -> {
                                                val localDate = currentDate.toLocalDateTime(timeZone).date
                                                "${localDate.month.name} ${localDate.year}"
                                            }

                                            "week" -> {
                                                val localDate = currentDate.toLocalDateTime(timeZone).date
                                                val startDate =
                                                    localDate.minus(DatePeriod(days = (localDate.dayOfWeek.isoDayNumber - 1) % 7))
                                                val endDate = startDate.plus(DatePeriod(days = 6))
                                                "${
                                                    startDate.month.name.substring(
                                                        0,
                                                        3
                                                    )
                                                } ${startDate.dayOfMonth} - ${
                                                    endDate.month.name.substring(
                                                        0,
                                                        3
                                                    )
                                                } ${endDate.dayOfMonth}, ${endDate.year}"
                                            }

                                            else -> {
                                                val localDate = currentDate.toLocalDateTime(timeZone).date
                                                "${localDate.dayOfWeek.name}, ${localDate.month.name} ${localDate.dayOfMonth}, ${localDate.year}"
                                            }
                                        },
                                        style= MaterialTheme.typography.subtitle2
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier.height(32.dp),
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
                    events = SessionState.state,
                    onEventClick = { openEditEventModal(it) }
                )

                "week" -> WeekView(
                    days = generateWeekDays(),
                    events = SessionState.state,
                    onEventClick = { openEditEventModal(it) }
                )

                "day" -> DayView(
                    date = currentDate,
                    events = emptyList(), // Cambiar a lista vacía por ahora
                    onEventClick = { /* No se necesita por ahora */ }
                )
            }
        }
    }

}
