package es.gaspardev.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.gaspardev.components.*
import es.gaspardev.core.Action
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.enums.StatusState
import es.gaspardev.icons.FitMeIcons
import es.gaspardev.layout.athletes.MeasurementItem
import es.gaspardev.layout.athletes.NutritionTab
import es.gaspardev.layout.athletes.OverviewTab
import es.gaspardev.layout.athletes.WorkoutsTab
import es.gaspardev.states.ConversationsState
import fit_me.composeapp.generated.resources.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun AthleteInfoScreen(
    athlete: Athlete
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("General", "Entrenamiento", "Nutrición")
    val router = LocalRouter.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with back and edit buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { router.navigateTo(Routes.Athletes) },
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Profile card (left column)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        UserAvatar(athlete.user, LayoutDirection.Vertical)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Status badge
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (athlete.user.status.state == StatusState.ACTIVE) Color(0xFF4CAF50) else Color.Gray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = if (athlete.user.status.state == StatusState.ACTIVE) stringResource(Res.string.active) else stringResource(
                                    Res.string.inactive
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Member since and last active
                        Text(
                            text = stringResource(Res.string.member_since) + " ${
                                athlete.trainingSince.toLocalDateTime(TimeZone.currentSystemDefault())
                                    .formatDateTime()
                            }",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )

                        LastActiveText(athlete.user.status.lastTimeActive)

                        if (athlete.allergies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            LazyRow(
                                modifier = Modifier.fillMaxWidth().heightIn(max = 150.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(athlete.allergies) { allergy ->
                                    AssistChip(
                                        label = { Text(allergy.name, style = MaterialTheme.typography.caption) })
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        if (athlete.getOverallProgression().toFloat() / 100f > 1) {
                            // Progress
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(Res.string.overall_progress),
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${athlete.getOverallProgression()}%",
                                    style = MaterialTheme.typography.body2
                                )
                            }


                            Spacer(modifier = Modifier.height(4.dp))

                            LinearProgressIndicator(
                                progress = athlete.getOverallProgression().toFloat() / 100f,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colors.primary,
                                backgroundColor = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        // Contact Information
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(Res.string.contact_info),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (athlete.user.phone != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colors.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = athlete.user.phone!!,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = athlete.user.email,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            // Current Plans
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(Res.string.current_plans),
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = FitMeIcons.Weight,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (athlete.workout == null) MaterialTheme.colors.onSurface.copy(.6f) else MaterialTheme.colors.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = athlete.workout?.name
                                            ?: stringResource(Res.string.no_workout_plan_assigned),
                                        style = MaterialTheme.typography.body2
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = FitMeIcons.Nutrition,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (athlete.diet == null) MaterialTheme.colors.onSurface.copy(.6f) else MaterialTheme.colors.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = athlete.diet?.name ?: stringResource(Res.string.no_diet_plan_assigned),
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Measurements
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(Res.string.measurements),
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    MeasurementItem(
                                        label = stringResource(Res.string.height),
                                        value = "${athlete.measurements.height} cm"
                                    )
                                    MeasurementItem(
                                        label = stringResource(Res.string.weight),
                                        value = " ${athlete.measurements.weight} Kg"
                                    )
                                    MeasurementItem(
                                        label = stringResource(Res.string.body_fat),
                                        value = "${athlete.measurements.bodyFat}%"
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                                // Action buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            router.executeAction(
                                                Action.SuspendAction.create {
                                                    ConversationsState.selectConversation(athlete.user)
                                                    router.navigateTo(Routes.Messages)
                                                }
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Message,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(stringResource(Res.string.send_message))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Details card (right column)
            Card(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(start = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Card header
                    Text(
                        text = "Athlete Details",
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "View and manage athlete information",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title, color = MaterialTheme.colors.onPrimary) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tab content
                    when (selectedTab) {
                        0 -> OverviewTab(athlete = athlete)
                        1 -> WorkoutsTab(athlete = athlete)
                        2 -> NutritionTab(athlete = athlete)
                    }
                }
            }
        }
    }
}




