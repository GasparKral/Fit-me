package es.gaspardev.layout.Calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.pages.CalendarEvent
import es.gaspardev.pages.getEventTypeColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern

@Composable
fun WeekView(
    days: List<LocalDate>,
    events: List<CalendarEvent>,
    onEventClick: (CalendarEvent) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        days.forEach { day ->
            val isToday = day == LocalDate.now()
            val dayEvents = events.filter { it.date == day }

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isToday) MaterialTheme.colors.primary
                            else MaterialTheme.colors.secondary
                        )
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.dayOfWeek.toString().take(3),
                            color = if (isToday) MaterialTheme.colors.onPrimary
                            else MaterialTheme.colors.secondary
                        )
                        Text(
                            text = day.dayOfMonth.toString(),
                            color = if (isToday) MaterialTheme.colors.onPrimary
                            else MaterialTheme.colors.secondary
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colors.primary)
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    if (dayEvents.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No events",
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    } else {
                        items(dayEvents) { event ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .clickable { onEventClick(event) },
                                backgroundColor = getEventTypeColor(event.type)

                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = event.title,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Row(
                                        modifier = Modifier.padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            FitMeIcons.Calendar,
                                            contentDescription = "Time",
                                            modifier = Modifier.size(12.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${event.startTime.format(ofPattern("HH:mm"))} - ${
                                                event.endTime.format(ofPattern("HH:mm"))
                                            }",
                                            fontSize = 12.sp,
                                            color = Color.White
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "Athlete",
                                            modifier = Modifier.size(12.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = event.athleteName,
                                            fontSize = 12.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
