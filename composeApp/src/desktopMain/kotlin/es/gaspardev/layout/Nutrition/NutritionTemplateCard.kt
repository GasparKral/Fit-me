package es.gaspardev.layout.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietDialog

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionTemplateCard(template: DietTemplate, onAcceptAction: (Diet) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlanBadge(icon = FitMeIcons.Nutrition, text = template.dietType.toString())
            }

            Text(
                text = "${template.dishes.values.size} dishes",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 16.dp)
            )

            Button(
                onClick = {
                    DialogState.openWith { DietDialog(template = template, onAcceptAction = onAcceptAction) }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Use Template")
            }
        }
    }
}