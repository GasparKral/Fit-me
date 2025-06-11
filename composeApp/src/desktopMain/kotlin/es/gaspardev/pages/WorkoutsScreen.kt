package es.gaspardev.pages

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.domain.usecases.create.workout.CreateNewWorkout
import es.gaspardev.core.domain.usecases.create.workout.CreateWorkoutTemplate
import es.gaspardev.core.domain.usecases.read.user.trainer.GetTrainerWorkoutsPlans
import es.gaspardev.core.domain.usecases.read.user.trainer.GetTrainerWorkoutsTemplates
import es.gaspardev.enums.WorkoutType
import es.gaspardev.helpers.createWorkout
import es.gaspardev.helpers.resWorkoutType
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.WorkoutDialog
import es.gaspardev.layout.workouts.WorkoutPlanCard
import es.gaspardev.layout.workouts.WorkoutTemplateCard
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun WorkoutsScreen(width: Dp) {
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf(WorkoutType.ALL) }
    var workoutPlans by remember { mutableStateOf<List<WorkoutPlan>>(emptyList()) }
    var workoutTemplates by remember { mutableStateOf<List<WorkoutTemplate>>(emptyList()) }

    // Carga inicial de datos
    LaunchedEffect(Unit) {
        loadInitialData { plans, templates ->
            workoutPlans = plans
            workoutTemplates = templates
        }
    }

    // Filtrado de planes
    val filteredPlans = remember(workoutPlans, searchQuery, activeTab) {
        filterWorkoutPlans(workoutPlans, searchQuery, activeTab)
    }

    // Estados de scroll
    val plansGridState = rememberLazyGridState()
    val templatesGridState = rememberLazyGridState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Sección de encabezado y controles
        HeaderSection(
            onCreateWorkout = {
                handleCreateWorkout(
                    scope = scope,
                    onSuccess = { newPlan -> workoutPlans = workoutPlans + newPlan }
                )
            },
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )

        Spacer(Modifier.height(12.dp))

        // Contenido principal
        MainContentCard(
            modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
            activeTab = activeTab,
            onTabChange = { activeTab = it },
            filteredPlans = filteredPlans,
            workoutTemplates = workoutTemplates,
            plansGridState = plansGridState,
            templatesGridState = templatesGridState,
            width = width,
            scope = scope,
            onCreateWorkout = {
                handleCreateWorkout(
                    scope = scope,
                    onSuccess = { newPlan -> workoutPlans = workoutPlans + newPlan }
                )
            },
            onEditPlan = { oldPlan, newPlan ->
                workoutPlans = workoutPlans - oldPlan + newPlan
            },
            onDuplicatePlan = { newPlan ->
                workoutPlans = workoutPlans + newPlan
            },
            onAssignPlan = { oldPlan, newPlan ->
                workoutPlans = workoutPlans - oldPlan + newPlan
            },
            onDeletePlan = { plan ->
                workoutPlans = workoutPlans - plan
            },
            onDeleteTemplate = { templateId ->
                workoutTemplates = workoutTemplates.filterNot { it.templateId == templateId }
            },
            onCreateFromTemplate = { newPlan ->
                workoutPlans = workoutPlans + newPlan
            },
            onCreateTemplate = { template ->
                workoutTemplates = workoutTemplates + template
            }
        )
    }
}

@Composable
private fun HeaderSection(
    onCreateWorkout: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título y botón de crear
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.workout_plans),
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(Res.string.workout_plans_description),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                CreateWorkoutButton(onClick = onCreateWorkout)
            }

            Spacer(Modifier.height(24.dp))

            // Barra de búsqueda
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange
            )
        }
    }
}

@Composable
private fun CreateWorkoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(Res.string.create_workout_plan))
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(Res.string.search_workout_plans)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        singleLine = true
    )
}

@Composable
private fun MainContentCard(
    modifier: Modifier,
    activeTab: WorkoutType,
    onTabChange: (WorkoutType) -> Unit,
    filteredPlans: List<WorkoutPlan>,
    workoutTemplates: List<WorkoutTemplate>,
    plansGridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    templatesGridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    width: Dp,
    scope: kotlinx.coroutines.CoroutineScope,
    onCreateWorkout: () -> Unit,
    onEditPlan: (WorkoutPlan, WorkoutPlan) -> Unit,
    onDuplicatePlan: (WorkoutPlan) -> Unit,
    onAssignPlan: (WorkoutPlan, WorkoutPlan) -> Unit,
    onDeletePlan: (WorkoutPlan) -> Unit,
    onDeleteTemplate: (Int) -> Unit,
    onCreateFromTemplate: (WorkoutPlan) -> Unit,
    onCreateTemplate: (WorkoutTemplate) -> Unit
) {
    Card(modifier) {
        Column {
            // Tabs de filtrado
            WorkoutTypeTabs(
                activeTab = activeTab,
                onTabChange = onTabChange
            )

            Spacer(Modifier.height(16.dp))

            // Sección de planes de entrenamiento
            WorkoutPlansSection(
                modifier = Modifier.fillMaxHeight(0.45f),
                filteredPlans = filteredPlans,
                gridState = plansGridState,
                width = width,
                scope = scope,
                onCreateWorkout = onCreateWorkout,
                onEditPlan = onEditPlan,
                onDuplicatePlan = onDuplicatePlan,
                onAssignPlan = onAssignPlan,
                onDeletePlan = onDeletePlan
            )

            Spacer(Modifier.height(32.dp))

            // Sección de plantillas
            WorkoutTemplatesSection(
                modifier = Modifier.fillMaxSize(),
                workoutTemplates = workoutTemplates,
                gridState = templatesGridState,
                width = width,
                scope = scope,
                onDeleteTemplate = onDeleteTemplate,
                onCreateFromTemplate = onCreateFromTemplate,
                onCreateTemplate = onCreateTemplate
            )
        }
    }
}

@Composable
private fun WorkoutTypeTabs(
    activeTab: WorkoutType,
    onTabChange: (WorkoutType) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = WorkoutType.entries.indexOf(activeTab).takeIf { it >= 0 } ?: 0,
        edgePadding = 0.dp
    ) {
        WorkoutType.entries.forEach { type ->
            Tab(
                selected = activeTab == type,
                onClick = { onTabChange(type) },
                text = { Text(resWorkoutType(type), color = MaterialTheme.colors.onPrimary) }
            )
        }
    }
}

@Composable
private fun WorkoutPlansSection(
    modifier: Modifier,
    filteredPlans: List<WorkoutPlan>,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    width: Dp,
    scope: kotlinx.coroutines.CoroutineScope,
    onCreateWorkout: () -> Unit,
    onEditPlan: (WorkoutPlan, WorkoutPlan) -> Unit,
    onDuplicatePlan: (WorkoutPlan) -> Unit,
    onAssignPlan: (WorkoutPlan, WorkoutPlan) -> Unit,
    onDeletePlan: (WorkoutPlan) -> Unit
) {
    Box(modifier) {
        if (filteredPlans.isNotEmpty()) {
            WorkoutPlansGrid(
                plans = filteredPlans,
                gridState = gridState,
                width = width,
                scope = scope,
                onEditPlan = onEditPlan,
                onDuplicatePlan = onDuplicatePlan,
                onAssignPlan = onAssignPlan,
                onDeletePlan = onDeletePlan
            )
        } else {
            EmptyPlansState(onCreateWorkout = onCreateWorkout)
        }
    }
}

@Composable
private fun WorkoutPlansGrid(
    plans: List<WorkoutPlan>,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    width: Dp,
    scope: kotlinx.coroutines.CoroutineScope,
    onEditPlan: (WorkoutPlan, WorkoutPlan) -> Unit,
    onDuplicatePlan: (WorkoutPlan) -> Unit,
    onAssignPlan: (WorkoutPlan, WorkoutPlan) -> Unit,
    onDeletePlan: (WorkoutPlan) -> Unit
) {
    Box {

        LazyVerticalGrid(
            columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
            modifier = Modifier.fillMaxSize().padding(12.dp),
            state = gridState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plans) { plan ->
                WorkoutPlanCard(
                    plan = plan,
                    scope = scope,
                    onEditAction = { newPlan -> onEditPlan(plan, newPlan) },
                    onDuplicationAction = onDuplicatePlan,
                    onAssignAction = { newPlan -> onAssignPlan(plan, newPlan) },
                    onDeleteAction = { onDeletePlan(plan) }
                )
            }
        }

        CustomScrollbar(
            gridState = gridState,
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@Composable
private fun EmptyPlansState(onCreateWorkout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = FitMeIcons.Weight,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color.LightGray
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.no_workout_plans_found),
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.create_first_workout),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(Modifier.height(16.dp))
        CreateWorkoutButton(onClick = onCreateWorkout)
    }
}

@Composable
private fun WorkoutTemplatesSection(
    modifier: Modifier,
    workoutTemplates: List<WorkoutTemplate>,
    gridState: LazyGridState,
    width: Dp,
    scope: CoroutineScope,
    onDeleteTemplate: (Int) -> Unit,
    onCreateFromTemplate: (WorkoutPlan) -> Unit,
    onCreateTemplate: (WorkoutTemplate) -> Unit
) {
    Column {
        // Header de plantillas
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.workout_templates),
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    handleCreateTemplate(
                        scope = scope,
                        onSuccess = onCreateTemplate
                    )
                }
            ) {
                Text("Crear nueva plantilla")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Grid de plantillas
        Box(modifier) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                state = gridState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(workoutTemplates) { template ->
                    WorkoutTemplateCard(
                        template = template,
                        scope = scope,
                        onDeleteAction = { onDeleteTemplate(template.templateId!!) },
                        onAcceptAction = { workout ->
                            handleCreateFromTemplate(
                                workout = workout,
                                scope = scope,
                                onSuccess = onCreateFromTemplate
                            )
                        }
                    )
                }
            }

            CustomScrollbar(
                gridState = gridState,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Composable
private fun BoxScope.CustomScrollbar(
    gridState: LazyGridState,
    modifier: Modifier
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(gridState),
        modifier = modifier
            .align(Alignment.CenterEnd)
            .padding(vertical = 16.dp),
        style = LocalScrollbarStyle.current.copy(
            hoverColor = MaterialTheme.colors.primaryVariant,
            unhoverColor = MaterialTheme.colors.primary
        )
    )
}

// Funciones auxiliares
private suspend fun loadInitialData(
    onResult: (List<WorkoutPlan>, List<WorkoutTemplate>) -> Unit
) {
    val trainer = LoggedTrainer.state.trainer ?: return

    val plans = GetTrainerWorkoutsPlans().run(trainer).getOrElse(emptyList())
    val templates = GetTrainerWorkoutsTemplates().run(trainer).getOrElse(emptyList())

    onResult(plans, templates)
}

private fun filterWorkoutPlans(
    plans: List<WorkoutPlan>,
    searchQuery: String,
    activeTab: WorkoutType
): List<WorkoutPlan> {
    return plans.filter { plan ->
        val matchesSearch = plan.name.contains(searchQuery, ignoreCase = true) ||
                plan.description.contains(searchQuery, ignoreCase = true)

        if (activeTab == WorkoutType.ALL) {
            matchesSearch
        } else {
            matchesSearch && plan.type == activeTab
        }
    }
}

private fun handleCreateWorkout(
    scope: CoroutineScope,
    onSuccess: (WorkoutPlan) -> Unit
) {
    DialogState.openWith {
        WorkoutDialog { workout ->
            scope.launch {
                CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer!!)).fold(
                    onSuccess = { workoutId ->
                        val newPlan = WorkoutPlan.fromWorkout(workout).copy(workoutId = workoutId)
                        onSuccess(newPlan)
                        ToastManager.showSuccess(
                            "La rutina se ha creado correctamente"
                        )
                        DialogState.close()
                    },
                    onFailure = { error ->
                        val message = error.message?.let { "Error al crear la rutina: $it" }
                            ?: "Error al crear la rutina"
                        ToastManager.showError(message)
                    }
                )
            }
        }
    }
}

private fun handleCreateTemplate(
    scope: CoroutineScope,
    onSuccess: (WorkoutTemplate) -> Unit
) {
    createWorkout { workout ->
        val template = WorkoutTemplate.fromWorkout(workout)

        scope.launch {
            CreateWorkoutTemplate().run(Pair(template, LoggedTrainer.state.trainer!!)).fold(
                onSuccess = { templateId ->
                    template.templateId = templateId
                    onSuccess(template)
                },
                onFailure = { error ->
                    ToastManager.showError(error.message ?: "Error al crear la plantilla")
                }
            )
        }
    }
}

private fun handleCreateFromTemplate(
    workout: es.gaspardev.core.domain.entities.workouts.Workout,
    scope: CoroutineScope,
    onSuccess: (WorkoutPlan) -> Unit
) {
    scope.launch {
        CreateWorkoutTemplate().run(
            Pair(WorkoutTemplate.fromWorkout(workout), LoggedTrainer.state.trainer!!)
        ).fold(
            onSuccess = { workoutId ->
                val newPlan = WorkoutPlan.fromWorkout(workout).copy(workoutId = workoutId)
                onSuccess(newPlan)
                ToastManager.showSuccess(
                    "La rutina se ha creado correctamente",
                )
                DialogState.close()
            },
            onFailure = { error ->
                val message = error.message?.let { "Error al crear la rutina: $it" }
                    ?: "Error al crear la rutina"
                ToastManager.showError(message)
            }
        )
    }
}