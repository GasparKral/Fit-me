package es.gaspardev.pages

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.dokar.sonner.ToastType
import com.dokar.sonner.rememberToasterState
import es.gaspardev.components.ToastManager
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.domain.usecases.create.CreateNewWorkout
import es.gaspardev.core.domain.usecases.create.CreateWorkoutTemplate
import es.gaspardev.core.domain.usecases.read.GetTrainerWorkoutsPlans
import es.gaspardev.core.domain.usecases.read.GetTrainerWorkoutsTemplates
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource


@Composable
fun WorkoutsScreen(width: Dp) {

    val toaster = rememberToasterState { }
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf(WorkoutType.ALL) }

    var workoutPlans by remember { mutableStateOf<List<WorkoutPlan>>(emptyList()) }
    var workoutTemplates by remember { mutableStateOf<List<WorkoutTemplate>>(emptyList()) }

    LaunchedEffect(Unit) {
        val trainer = LoggedTrainer.state.trainer!!

        GetTrainerWorkoutsPlans().run(trainer).fold(
            { value -> workoutPlans = value }
        )

        GetTrainerWorkoutsTemplates().run(trainer).fold(
            { value -> workoutTemplates = value }
        )
    }

    val filteredPlans = workoutPlans.filter { plan ->
        val matchesSearch = plan.name.contains(searchQuery, ignoreCase = true) ||
                plan.description.contains(searchQuery, ignoreCase = true)

        if (activeTab == WorkoutType.ALL) {
            matchesSearch
        } else {
            matchesSearch && plan.type == activeTab
        }
    }

    val lazyGridState1 = rememberLazyGridState()
    val lazyGridState2 = rememberLazyGridState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Card {
            // Header section
            Column(modifier = Modifier.padding(16.dp)) {
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

                    Button(
                        onClick = {
                            DialogState.openWith {
                                WorkoutDialog { workout ->
                                    scope.launch {
                                        CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer!!)).fold(
                                            { value ->
                                                workoutPlans = workoutPlans + (
                                                        WorkoutPlan(
                                                            workoutId = value,
                                                            name = workout.name,
                                                            description = workout.description,
                                                            type = workout.workoutType,
                                                            duration = workout.duration,
                                                            frequency = workout.exercises.keys.count().toString(),
                                                            difficulty = workout.difficulty,
                                                            asignedCount = 0,
                                                            exercises = workout.exercises
                                                        )
                                                        )

                                                DialogState.close()
                                                toaster.show(
                                                    "La rutina se ha creado correctamente",
                                                    type = ToastType.Success
                                                )
                                            },
                                            { err ->
                                                toaster.show(
                                                    if (err.message != null) "Error al crear la rutina" else "Error al crear la rutina: ${err.message!!}",
                                                    type = ToastType.Error
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        contentPadding = ButtonDefaults.ContentPadding
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(Res.string.create_workout_plan))
                    }

                }

                Spacer(Modifier.height(24.dp))

                // Search section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(stringResource(Res.string.search_workout_plans)) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        singleLine = true
                    )
                }

            }
        }
        Spacer(Modifier.height(12.dp))

        Card(Modifier.fillMaxSize().padding(bottom = 16.dp)) {
            Column {
                // Tabs section
                ScrollableTabRow(
                    selectedTabIndex = WorkoutType.entries.indexOfFirst { it == WorkoutType.ALL }.takeIf { it >= 0 }
                        ?: 0,
                    edgePadding = 0.dp
                ) {
                    WorkoutType.entries.forEachIndexed { _, type ->
                        Tab(
                            selected = activeTab == type,
                            onClick = { activeTab = type },
                            text = { Text(resWorkoutType(type)) }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Workout plans
                Box(Modifier.fillMaxHeight(.45f).fillMaxWidth()) {
                    if (filteredPlans.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            state = lazyGridState1,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredPlans) { plan ->
                                WorkoutPlanCard(
                                    plan, scope,
                                    onEditAction = { newDietPlan ->
                                        workoutPlans = workoutPlans - plan + newDietPlan
                                    },
                                    onDuplicationAction = { newDiplicatePlan ->
                                        workoutPlans = workoutPlans + newDiplicatePlan
                                    },
                                    onAsign = { newRevaluedPlan ->
                                        workoutPlans = workoutPlans - plan + newRevaluedPlan
                                    },
                                    onDeleteAction = {
                                        workoutPlans = workoutPlans - plan
                                    })
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(lazyGridState1),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                                .padding(vertical = 16.dp),
                            style = LocalScrollbarStyle.current.copy(
                                hoverColor = MaterialTheme.colors.primaryVariant,
                                unhoverColor = MaterialTheme.colors.primary
                            )
                        )
                    } else {
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
                                text = if (searchQuery.isNotEmpty()) stringResource(Res.string.try_different_search)
                                else stringResource(Res.string.create_first_workout),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onSurface
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    createWorkout { workout ->
                                        scope.launch {
                                            CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer!!))
                                                .fold(
                                                    { value ->
                                                        workoutPlans =
                                                            workoutPlans + WorkoutPlan.fromWorkout(workout.copy(id = value))
                                                        toaster.show(
                                                            "La rutina se ha creado correctamente",
                                                            type = ToastType.Success
                                                        )
                                                    },
                                                    { err ->
                                                        toaster.show(
                                                            if (err.message != null) "Error al crear la rutina" else "Error al crear la rutina: ${err.message!!}",
                                                            type = ToastType.Error
                                                        )
                                                    }
                                                )
                                        }
                                    }
                                },
                                contentPadding = ButtonDefaults.ContentPadding
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(stringResource(Res.string.create_workout_plan))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.workout_templates),
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Bold
                    )
                    Button(onClick = {
                        createWorkout { workout ->
                            val template = WorkoutTemplate.fromWorkout(workout)

                            scope.launch {
                                CreateWorkoutTemplate().run(Pair(template, LoggedTrainer.state.trainer!!)).fold(
                                    { newId ->
                                        workoutTemplates = workoutTemplates + template.copy(templateId = newId)
                                    },
                                    { err -> ToastManager.showError(err.message!!) }
                                )
                            }
                        }
                    }) {
                        Text("Crear nueva plantilla")
                    }
                }
                Spacer(Modifier.height(16.dp))
                Box(Modifier.fillMaxSize()) {
                    // Workout templates section
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(if (width > 1440.dp) 4 else 3),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        state = lazyGridState2,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(workoutTemplates) { template ->
                            WorkoutTemplateCard(template = template, scope, { id ->
                                workoutTemplates = workoutTemplates - workoutTemplates.first { it.getId() == id }
                            }) { workout ->
                                scope.launch {
                                    CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer!!))
                                        .fold(
                                            { value ->
                                                workoutPlans = workoutPlans + (
                                                        WorkoutPlan(
                                                            workoutId = value,
                                                            name = workout.name,
                                                            description = workout.description,
                                                            type = workout.workoutType,
                                                            duration = workout.duration,
                                                            frequency = workout.exercises.keys.count()
                                                                .toString(),
                                                            difficulty = workout.difficulty,
                                                            asignedCount = 0,
                                                            exercises = workout.exercises
                                                        )
                                                        )
                                                toaster.show(
                                                    "La rutina se ha creado correctamente",
                                                    type = ToastType.Success
                                                )
                                            },
                                            { err -> // Error
                                                toaster.show(
                                                    if (err.message != null) "Error al crear la rutina" else "Error al crear la rutina: ${err.message!!}",
                                                    type = ToastType.Error
                                                )
                                            }
                                        )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(lazyGridState2),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .padding(vertical = 16.dp),
                        style = LocalScrollbarStyle.current.copy(
                            hoverColor = MaterialTheme.colors.primaryVariant,
                            unhoverColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
    }
}


