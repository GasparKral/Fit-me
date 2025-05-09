package es.gaspardev.layout.Workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.components.DifficultyBadge
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.pages.WorkoutTemplate


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutTemplateCard(template: WorkoutTemplate) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = template.description,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(16.dp))
            DifficultyBadge(template.difficulty)
            // Badges row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(template.type) },
                    leadingIcon = {
                        Icon(
                            FitMeIcons.Weight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "${template.exercises} exercises",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(Modifier.height(16.dp))

            Divider()

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { /* Use template action */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Use Template")
            }
        }
    }
}