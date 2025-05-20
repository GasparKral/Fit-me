package es.gaspardev.layout.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.pages.CalendarEvent
import es.gaspardev.pages.getEventTypeColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MonthView(
    currentDate: LocalDate,
    days: List<LocalDate>,
    events: List<CalendarEvent>,
    onEventClick: (CalendarEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize()
        ) {
            items(days) { day ->
                val isCurrentMonth = day.month == currentDate.month
                val isToday = day == LocalDate.now()
                val dayEvents = events.filter { it.date == day }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(
                            1.dp,
                            if (isToday) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                            RoundedCornerShape(4.dp)
                        )
                        .background(
                            if (isCurrentMonth) MaterialTheme.colors.surface
                            else MaterialTheme.colors.onSecondary.copy(alpha = 0.3f)
                        )
                        .padding(4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = day.dayOfMonth.toString(),
                            modifier = Modifier
                                .align(Alignment.End)
                                .background(
                                    if (isToday) MaterialTheme.colors.primary
                                    else Color.Transparent,
                                    CircleShape
                                )
                                .padding(4.dp)
                                .size(24.dp),
                            color = if (isToday) MaterialTheme.colors.onPrimary
                            else MaterialTheme.colors.onSurface,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )

                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(dayEvents.take(3)) { event ->
                                Text(
                                    text = "${event.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} ${event.title}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(getEventTypeColor(event.type))
                                        .clickable { onEventClick(event) }
                                        .padding(4.dp),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                            }

                            if (dayEvents.size > 3) {
                                item {
                                    Text(
                                        text = "+${dayEvents.size - 3} more",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center
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