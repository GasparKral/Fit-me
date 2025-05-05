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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.core.Routing.RouterController
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.FloatingDialog
import es.gaspardev.layout.Workouts.WorkoutPlanCard
import es.gaspardev.layout.Workouts.WorkoutTemplateCard

@Composable
fun WorkoutsScreen(controller: RouterController) {

    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("all") }

    val workoutPlans = remember {
        listOf(
            WorkoutPlan(
                id = "1",
                name = "Strength Building Program",
                description = "A comprehensive program focused on building overall strength",
                type = "strength",
                duration = "8 weeks",
                frequency = "4 days/week",
                difficulty = "intermediate",
                assignedCount = 8,
                lastUpdated = "2 days ago",
                exercises = 24
            ),
            // ... other workout plans
        )
    }

    val workoutTemplates = remember {
        listOf(
            WorkoutTemplate(
                id = "t1",
                name = "5x5 Strength Program",
                description = "Classic 5x5 strength building template",
                type = "strength",
                difficulty = "intermediate",
                exercises = 15
            ),
            // ... other templates
        )
    }

    val filteredWorkouts = workoutPlans.filter { plan ->
        val matchesSearch = plan.name.contains(searchQuery, ignoreCase = true) ||
                plan.description.contains(searchQuery, ignoreCase = true)

        if (activeTab == "all") matchesSearch else matchesSearch && plan.type == activeTab
    }
    val scrollState = rememberScrollState()
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState)
    )
    FloatingDialog {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Create New Workout Plan",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Form fields would go here

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { DialogState.close() }) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { /* Save action */ }) {
                        Text("Create Plan")
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Header section
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
                onClick = { DialogState.open() },
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
                Text("Filter")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Tabs section
        ScrollableTabRow(
            selectedTabIndex = when (activeTab) {
                "all" -> 0
                "strength" -> 1
                "cardio" -> 2
                "full-body" -> 3
                "core" -> 4
                else -> 0
            },
            edgePadding = 0.dp
        ) {
            listOf("All Plans", "Strength", "Cardio", "Full Body", "Core").forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == when (index) {
                        0 -> "all"
                        1 -> "strength"
                        2 -> "cardio"
                        3 -> "full-body"
                        4 -> "core"
                        else -> "all"
                    },
                    onClick = {
                        activeTab = when (index) {
                            0 -> "all"
                            1 -> "strength"
                            2 -> "cardio"
                            3 -> "full-body"
                            4 -> "core"
                            else -> "all"
                        }
                    },
                    text = { Text(title) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Workout plans grid
        if (filteredWorkouts.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(vertical = 12.dp),
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
                    tint = MaterialTheme.colors.onSurface
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
                    onClick = { DialogState.open() },
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
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.heightIn(max = 800.dp, min = 400.dp).padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(workoutTemplates) { template ->
                WorkoutTemplateCard(template = template)
            }
        }
    }

}

data class WorkoutPlan(
    val id: String,
    val name: String,
    val description: String,
    val type: String,
    val duration: String,
    val frequency: String,
    val difficulty: String,
    val assignedCount: Int,
    val lastUpdated: String,
    val exercises: Int
)

data class WorkoutTemplate(
    val id: String,
    val name: String,
    val description: String,
    val type: String,
    val difficulty: String,
    val exercises: Int
)