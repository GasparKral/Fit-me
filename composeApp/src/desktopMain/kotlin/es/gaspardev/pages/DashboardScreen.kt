package es.gaspardev.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import es.gaspardev.core.Action
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.dtos.TrainerDashBoardInfo
import es.gaspardev.core.domain.usecases.read.LoadDashboardInfo
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.dashboard.AthletesList
import es.gaspardev.layout.dashboard.QuickActions
import es.gaspardev.layout.dashboard.StatCard
import es.gaspardev.layout.dashboard.StatisticsChart
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardScreen() {

    val router = LocalRouter.current
    val toaster = rememberToasterState()
    var info = TrainerDashBoardInfo()
    val scope = rememberCoroutineScope()

    LaunchedEffect(LoggedTrainer.state.trainer) {
        LoggedTrainer.state.trainer?.let {
            LoadDashboardInfo().run(it).fold(
                { res -> info = res },
                { _ -> }
            )
        }
    }

    val statisticsCardInfo = listOf(
        Triple(
            stringResource(Res.string.total_athletes),
            "${LoggedTrainer.state.athletes!!.size}",
            String.format(stringResource(Res.string.total_athletes_description), info.newsSportsman)
        ) to Icons.Filled.Person,
        Triple(
            stringResource(Res.string.active_plans),
            "${info.activePlans}",
            String.format(stringResource(Res.string.active_plans_description), info.newsPlans)
        ) to FitMeIcons.Weight,
        Triple(
            stringResource(Res.string.upcoming_sessions),
            "${info.upcommingSessions}",
            stringResource(Res.string.upcoming_sessions_description)
        ) to FitMeIcons.Calendar,
        Triple(
            stringResource(Res.string.unread_messages),
            "${info.unreadMessages}",
            String.format(stringResource(Res.string.unread_messages_description), info.newMessages)
        ) to Icons.Filled.Notifications
    )

    val scrollState = rememberScrollState()
    Box {
        Toaster(toaster, Modifier.align(Alignment.TopEnd))
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(horizontal = 4.dp, vertical = 16.dp),
            style = LocalScrollbarStyle.current.copy(
                hoverColor = MaterialTheme.colors.primaryVariant,
                unhoverColor = MaterialTheme.colors.primary
            )
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
                            text = stringResource(Res.string.dashboard_bienvenida) + " " + LoggedTrainer.state.trainer!!.user.fullname,
                            style = MaterialTheme.typography.h1,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format(
                                stringResource(Res.string.dashboard_pendientes),
                                info.pendingWorkouts
                            ),
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            }

            // Quick Actions
            QuickActions(router, toaster)

            // Stats Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                statisticsCardInfo.forEach { (data, icon) ->
                    item {
                        StatCard(
                            title = data.first,
                            value = data.second,
                            description = data.third,
                            icon = icon,
                            footer = {
                                if (data.first == stringResource(Res.string.unread_messages)) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextButton(
                                        onClick = {
                                            router.navigateTo(Routes.Messages)
                                        },
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colors.primary
                                        )
                                    ) {
                                        Text(stringResource(Res.string.view_messages))
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
                                    text = stringResource(Res.string.performance_overview),
                                    style = MaterialTheme.typography.h1,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(Res.string.performance_description),
                                    style = MaterialTheme.typography.subtitle1,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
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
                                    text = stringResource(Res.string.your_athletes),
                                    style = MaterialTheme.typography.h1,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(Res.string.athletes_description),
                                    style = MaterialTheme.typography.subtitle1,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Button(
                                onClick = {
                                    router.executeAction(Action.SuspendAction.create {
                                        router.navigateTo(Routes.Athletes)
                                        agregateNewSportman(toaster)
                                    })
                                },
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
                                Text(stringResource(Res.string.add_athlete))

                            }
                        }

                        AthletesList(scope)
                    }
                }
            }
        }

    }

}
