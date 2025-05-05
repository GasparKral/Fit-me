package es.gaspardev.layout.Workouts

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.pages.WorkoutPlan
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutPlanCard(plan: WorkoutPlan) {
    var showDropdown by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = plan.description,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box {
                    IconButton(
                        onClick = { showDropdown = true }
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Actions")
                    }

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        DropdownMenuItem(
                            onClick = { /* Edit action */ }
                        ) { Text("Edit Plan") }
                        DropdownMenuItem(
                            onClick = { /* Duplicate action */ }
                        ) { Text("Duplicate Plan") }
                        DropdownMenuItem(
                            onClick = { /* Assign action */ }
                        ) { Text("Assign to Athlete") }
                        Divider()
                        DropdownMenuItem(
                            onClick = { /* Delete action */ }
                        ) {
                            Text("Delete Plan", color = MaterialTheme.colors.error)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Badge(
                backgroundColor = when (plan.difficulty) {
                    "beginner" -> Color(0xFFE8F5E9)
                    "intermediate" -> Color(0xFFE3F2FD)
                    "advanced" -> Color(0xFFFFEBEE)
                    else -> Color(0xFFF5F5F5)
                },
                contentColor = when (plan.difficulty) {
                    "beginner" -> Color(0xFF2E7D32)
                    "intermediate" -> Color(0xFF1565C0)
                    "advanced" -> Color(0xFFC62828)
                    else -> Color(0xFF424242)
                }
            ) {
                Text(plan.difficulty.replaceFirstChar { it.uppercase() })
            }
            // Badges row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(plan.type) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Weight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(plan.duration) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Calendar,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

                AssistChip(
                    onClick = {},
                    label = { Text(plan.frequency) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )


            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${plan.exercises} exercises",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "Updated ${plan.lastUpdated}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Spacer(Modifier.height(16.dp))

            Divider()

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.onSurface
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Assigned to ${plan.assignedCount} athletes",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                TextButton(onClick = { /* View details action */ }) {
                    Text("View Details")
                }
            }
        }
    }
}