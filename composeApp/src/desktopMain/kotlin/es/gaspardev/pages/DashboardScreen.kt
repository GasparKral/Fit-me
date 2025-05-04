package es.gaspardev.pages

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.controllers.LoggedUser
import es.gaspardev.core.Routing.RouterController
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.Dashboard.*

@Composable
fun DashboardScreen(controller: RouterController) {

    val scrollState = rememberScrollState()
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState)
    )
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Bienvenido de vuelta ${LoggedUser.user.name}",
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "You have 3 athletes waiting for workout plans.",
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.9f)
                    )
                }
                Button(
                    onClick = { /* Create new plan */ },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onPrimary,
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create New Plan")
                }
            }
        }

        // Quick Actions
        QuickActions(controller)

        // Stats Cards
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf(
                Triple("Total Athletes", "24", "+2 from last month") to Icons.Filled.Person,
                Triple("Active Plans", "18", "+3 from last month") to FitMeIcons.Weight,
                Triple("Upcoming Sessions", "7", "For the next 48 hours") to FitMeIcons.Calendar,
                Triple("Unread Messages", "12", "5 new since yesterday") to Icons.Filled.Notifications
            ).forEach { (data, icon) ->
                item {
                    StatCard(
                        title = data.first,
                        value = data.second,
                        description = data.third,
                        icon = icon,
                        footer = {
                            if (data.first == "Unread Messages") {
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(
                                    onClick = { /* View messages */ },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colors.primary
                                    )
                                ) {
                                    Text("View Messages")
                                }
                            }
                        }
                    )
                }
            }
        }

        // Performance and Athletes Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Performance Overview
            Card(
                modifier = Modifier.weight(4f)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Performance Overview",
                                style = MaterialTheme.typography.h2,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Athlete progress metrics for the last 30 days",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        OutlinedButton(
                            onClick = { /* Export data */ },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colors.primary
                            )
                        ) {
                            Text("Export Data")
                        }
                    }
                    // Chart would go here
                    StatisticsChart()
                }
            }

            // Your Athletes
            Card(
                modifier = Modifier.weight(3f)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Your Athletes",
                                style = MaterialTheme.typography.h2,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Manage your athletes and their progress",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Button(
                            onClick = { /* Add athlete */ },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Athlete")

                        }
                    }
                    // Athletes list would go here
                    AthletesList()
                }
            }
        }

        // Recent Activities
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Recent Activities",
                            style = MaterialTheme.typography.h2,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Latest updates from your athletes",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    OutlinedButton(
                        onClick = { /* View all */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Text("View All")
                    }
                }
                // Activities would go here
                RecentActivities()
            }
        }
    }

}


