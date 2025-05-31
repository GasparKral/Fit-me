package es.gaspardev.layout.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.components.DropdownMenuButton
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.icons.FitMeIcons

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionPlanCard(
    plan: DietPlan,
    modifier: Modifier = Modifier,
    /*onViewDetails: () -> Unit,
    onEdit: () -> Unit*/
) {
    Card(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = plan.description,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        maxLines = 2,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                DropdownMenuButton(
                    items = listOf("Edit Plan", "Duplicate Plan", "Assign to Athlete", "Delete Plan"),
                    onItemSelected = { index ->
                        when (index) {
                            0 -> {}
                            3 -> { /* Handle delete */
                            }
                            // Handle other actions
                        }
                    }
                )
            }

            FlowRow(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    {},
                    label = { Text(plan.type.toString()) },
                    leadingIcon = { Icon(FitMeIcons.Nutrition, "Icons of ${plan.type}") })
                AssistChip(
                    {},
                    label = { Text(plan.duration) },
                    leadingIcon = { Icon(FitMeIcons.Calendar, "Icons of ${plan.duration}") })
                AssistChip(
                    {},
                    label = { Text("${plan.dishes.values.sumOf { it.size }} meals/day") },
                    leadingIcon = { Icon(Icons.Default.Refresh, "Icons of meals per day") })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${plan.dishes.values.size} dishes",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Assigned",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Assigned to ${plan.asignedCount} athletes",
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                TextButton(onClick = {}) {
                    Text("View Details")
                }
            }
        }
    }
}