package es.gaspardev.layout.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AssistChip
import es.gaspardev.components.DropdownMenuButton
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.usecases.create.CreateNewDiet
import es.gaspardev.core.domain.usecases.delete.DeleteDiet
import es.gaspardev.core.domain.usecases.update.AssignDietToAthlete
import es.gaspardev.core.domain.usecases.update.UpdateDiet
import es.gaspardev.helpers.resDietType
import es.gaspardev.helpers.visualizeDiet
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.AsignDialog
import es.gaspardev.layout.dialogs.DietDialog
import es.gaspardev.states.LoggedTrainer
import es.gaspardev.utils.toWeeks
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.weeks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionPlanCard(
    plan: DietPlan,
    scope: CoroutineScope,
    onDuplicationAction: (DietPlan) -> Unit = {},
    onAsign: (DietPlan) -> Unit = {},
    onEditAction: (DietPlan) -> Unit = {},
    onDeleteAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 4.dp
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
                    items = listOf(
                        { Text("Edit Plan", style = MaterialTheme.typography.subtitle2) },
                        { Text("Duplicate Plan", style = MaterialTheme.typography.subtitle2) },
                        { Text("Assign to Athlete", style = MaterialTheme.typography.subtitle2) },
                        { Text("Delete Plan", style = MaterialTheme.typography.subtitle2) }
                    ),
                    onItemSelected = { index, continueOpen ->
                        when (index) {
                            0 -> {
                                DialogState.openWith {
                                    DietDialog(
                                        Diet(
                                            id = plan.dietId,
                                            name = plan.name,
                                            description = plan.description,
                                            duration = plan.duration,
                                            startAt = Instant.DISTANT_FUTURE,
                                            dietType = plan.type,
                                            dishes = plan.dishes
                                        )
                                    ) {
                                        scope.launch {
                                            UpdateDiet().run(plan).fold(
                                                { value -> onEditAction(value) },
                                                { err -> ToastManager.showError(err.message!!) }
                                            )
                                        }
                                    }
                                }
                                continueOpen(false)
                            }

                            1 -> {
                                val diet = Diet(
                                    null,
                                    name = plan.name + " copy",
                                    description = plan.description,
                                    duration = plan.duration,
                                    startAt = Instant.DISTANT_FUTURE,
                                    dietType = plan.type,
                                    dishes = plan.dishes
                                )

                                scope.launch {
                                    CreateNewDiet().run(Pair(diet, LoggedTrainer.state.trainer!!)).fold(
                                        { value ->
                                            onDuplicationAction(
                                                plan.copy(
                                                    dietId = value,
                                                    name = plan.name + " copy"
                                                )
                                            )
                                        },
                                        { err -> ToastManager.showError(err.message!!) }
                                    )
                                }
                                continueOpen(false)
                            }

                            2 -> {
                                DialogState.openWith {
                                    AsignDialog(
                                        {
                                            Column {
                                                Text(plan.name)
                                                Text(plan.description)
                                            }
                                        },
                                        onAcceptAction = {
                                            scope.launch {
                                                AssignDietToAthlete().run(Pair(plan, it)).fold(
                                                    { _ -> onAsign(plan.copy(asignedCount = plan.asignedCount + 1)) },
                                                    { err -> ToastManager.showError("Ocurrió un erro al actualizar${if (err.message != null) ": ${err.message!!}" else ""}") }
                                                )
                                            }

                                        },
                                        onCancel = {}
                                    )
                                }
                                continueOpen(false)
                            }

                            3 -> {
                                scope.launch {
                                    DeleteDiet().run(plan.dietId).fold(
                                        { onDeleteAction() },
                                        { err -> ToastManager.showError(err.message!!) }
                                    )
                                }
                                continueOpen(false)
                            }
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
                    label = { Text(resDietType(plan.type)) },
                    leadingIcon = { Icon(FitMeIcons.Nutrition, "Icons of ${plan.type}") })
                AssistChip(
                    label = { Text(plan.duration.toWeeks().format(stringResource(Res.string.weeks))) },
                    leadingIcon = { Icon(FitMeIcons.Calendar, "Icons of ${plan.duration}") })
                AssistChip(
                    label = { Text("${plan.dishes.values.sumOf { it.size } / plan.dishes.keys.size} platos/día") },
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

                TextButton(onClick = {
                    visualizeDiet(Diet.fromPlan(plan))
                }) {
                    Text("View Details")
                }
            }
        }
    }
}