package es.gaspardev.pages

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.domain.usecases.create.CreateNewWorkout
import es.gaspardev.core.domain.usecases.read.GetTrainerWorkoutsPlans
import es.gaspardev.core.domain.usecases.read.GetTrainerWorkoutsTemplates
import es.gaspardev.core.infrastructure.repositories.WorkoutRespositoryImp
import es.gaspardev.enums.WorkoutType
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.WorkoutCreationDialog
import es.gaspardev.layout.workouts.WorkoutPlanCard
import es.gaspardev.layout.workouts.WorkoutTemplateCard
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.all
import fit_me.composeapp.generated.resources.filters
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource


@Composable
fun WorkoutsScreen() {

    LocalRouter.current
    val scope = rememberCoroutineScope()
    val tabs = listOf(
        WorkoutType.ALL to stringResource(Res.string.all),
        WorkoutType.STRENGTH to "Strength",
        WorkoutType.CARDIO to "Cardio",
        WorkoutType.CORE to "Core",
        WorkoutType.FLEXIBILITY to "Flexibility",
        WorkoutType.FULL_BODY to "Full Body",
        WorkoutType.LOWER_BODY to "Lower Body",
        WorkoutType.UPPER_BODY to "Upper Body"
    )

    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf(WorkoutType.ALL) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var workoutPlans: MutableList<WorkoutPlan> = remember { mutableListOf() }
    var workoutTemplates: MutableList<WorkoutTemplate> = remember { mutableListOf() }

    LaunchedEffect(Unit) {
        val trainer = LoggedTrainer.state.trainer!!

        GetTrainerWorkoutsPlans(WorkoutRespositoryImp()).run(trainer).fold(
            { value -> workoutPlans = value.toMutableList() }
        )

        GetTrainerWorkoutsTemplates(WorkoutRespositoryImp()).run(trainer).fold(
            { value -> workoutTemplates = value.toMutableList() }
        )
    }

    val filteredWorkouts = workoutPlans.filter { plan ->
        val matchesSearch = plan.name.contains(searchQuery, ignoreCase = true) ||
                plan.description.contains(searchQuery, ignoreCase = true)

        if (activeTab == WorkoutType.ALL) {
            matchesSearch
        } else {
            matchesSearch && plan.type == activeTab
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            title = { Text("Error al crear la rutina") },
            text = { Text(errorMessage) },
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    val scrollState = rememberScrollState()
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
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
                            text = "Workout Plans",
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Create and manage workout plans for your athletes",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    Button(
                        onClick = {
                            DialogState.openWith {
                                WorkoutCreationDialog { workout ->
                                    // Launch coroutine for suspend function
                                    scope.launch {
                                        CreateNewWorkout().run(Pair(workout, LoggedTrainer.state.trainer!!)).fold(
                                            { // Success
                                                workoutPlans.addLast(
                                                    WorkoutPlan(
                                                        name = workout.name,
                                                        description = workout.description,
                                                        type = workout.workoutType,
                                                        duration = workout.duration.toIsoString(),
                                                        frequency = workout.exercises.keys.count().toString(),
                                                        difficulty = workout.difficulty,
                                                        assignedCount = 0,
                                                        exercises = workout.exercises
                                                    )
                                                )
                                            },
                                            { err -> // Error
                                                errorMessage = err.message ?: "Unknown error"
                                                showErrorDialog = true
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
                        Text("Create Workout Plan")
                    }

                }

                Spacer(Modifier.height(24.dp))

                // Search and filter section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search workout plans...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        singleLine = true
                    )

                    OutlinedButton(
                        onClick = { /* Filter action */ }
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(Res.string.filters))
                    }
                }

            }
        }
        Spacer(Modifier.height(12.dp))

        Card(Modifier.fillMaxSize()) {
            Column {
                // Tabs section
                ScrollableTabRow(
                    selectedTabIndex = tabs.indexOfFirst { it.first == activeTab }.takeIf { it >= 0 } ?: 0,
                    edgePadding = 0.dp
                ) {
                    tabs.forEachIndexed { _, (workoutType, title) ->
                        Tab(
                            selected = activeTab == workoutType,
                            onClick = { activeTab = workoutType },
                            text = { Text(title) }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Workout plans grid
                if (filteredWorkouts.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredWorkouts) { plan ->
                            WorkoutPlanCard(plan = plan)
                        }
                    }
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
                            text = "No workout plans found",
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "Try a different search term"
                            else "Create your first workout plan to get started",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                DialogState.openWith { WorkoutCreationDialog(onCreatePlan = { }) }
                            },
                            contentPadding = ButtonDefaults.ContentPadding
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Create Workout Plan")
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Workout templates section
                Text(
                    text = "Workout Templates",
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp, start = 12.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(workoutTemplates) { template ->
                        WorkoutTemplateCard(template = template)
                    }
                }
            }

        }
    }

}


