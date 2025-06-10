package es.gaspardev.layout.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.core.domain.usecases.delete.DeleteDietTemplate
import es.gaspardev.helpers.resDietType
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionTemplateCard(
    template: DietTemplate,
    scope: CoroutineScope,
    onDeleteAction: (Int) -> Unit,
    onAcceptAction: (Diet) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                PlanBadge(icon = FitMeIcons.Nutrition, text = resDietType(template.dietType))
            }

            Text(
                text = "${template.dishes.values.size} dishes",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 16.dp)
            )
            Row {

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            DeleteDietTemplate().run(template.getId()).fold(
                                { _ -> onDeleteAction(template.getId()) },
                                { err -> ToastManager.showError(err.message!!) }
                            )
                        }
                    }
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Elimar Template")
                }
                Spacer(Modifier.width(6.dp))
                Button(
                    onClick = {
                        DialogState.openWith { DietDialog(template = template, onAcceptAction = onAcceptAction) }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Usar Template")
                }
            }
        }
    }
}