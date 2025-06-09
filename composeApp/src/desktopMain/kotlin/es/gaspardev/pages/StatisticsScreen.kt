package es.gaspardev.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.components.ToastManager
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.domain.entities.stadistics.*
import es.gaspardev.core.domain.entities.stadistics.generatePerformanceReport
import es.gaspardev.core.domain.entities.stadistics.generateReportFilename
import es.gaspardev.core.domain.entities.stadistics.toFormattedReport
import es.gaspardev.core.domain.usecases.read.GetAthleteStadistics
import es.gaspardev.helpers.formatAsPercentage
import es.gaspardev.layout.statistics.*
import es.gaspardev.states.LoggedTrainer
import es.gaspardev.utils.saveTextFile
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@Composable
fun StatisticsScreen() {
    var selectedAthlete by remember { mutableStateOf(LoggedTrainer.state.athletes?.first()!!) }
    var timeRange by remember { mutableStateOf(30.toDuration(DurationUnit.DAYS)) }
    var activeTab by remember { mutableStateOf("strength") }
    var statistics: ComprehensiveStatistics? by remember { mutableStateOf(null) }

    // Estados para el autocomplete
    var searchText by remember { mutableStateOf(selectedAthlete.user.fullname) }
    var expanded by remember { mutableStateOf(false) }

    // Filtrar atletas basándose en el texto de búsqueda
    val filteredAthletes by remember {
        derivedStateOf {
            if (searchText.isBlank()) {
                LoggedTrainer.state.athletes ?: emptyList()
            } else {
                LoggedTrainer.state.athletes?.filter { athlete ->
                    athlete.user.fullname.contains(searchText, ignoreCase = true)
                } ?: emptyList()
            }
        }
    }

    LaunchedEffect(selectedAthlete, timeRange) {
        GetAthleteStadistics().run(Pair(selectedAthlete, timeRange)).fold(
            { value -> statistics = value },
            { err -> ToastManager.showError(err.message!!) }
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Historial de Progreso",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Seguimiento de la evolución de tus atletas",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            val report = selectedAthlete.generatePerformanceReport(
                                statistics?.strengthStats ?: emptyList(),
                                statistics?.enduranceStats ?: emptyList(),
                                statistics?.measurementHistory ?: emptyList(),
                                LoggedTrainer.state.trainer!!.user.fullname,
                                LoggedTrainer.state.trainer!!.user.email
                            )

                            val reportName = report.generateReportFilename("txt")
                            val stringifyReport = report.toFormattedReport()

                            saveTextFile(stringifyReport, reportName)
                        },
                        border = BorderStroke(2.dp, MaterialTheme.colors.primary)
                    ) {
                        Icon(Icons.Default.AccountBox, contentDescription = "Export")
                        Spacer(Modifier.width(8.dp))
                        Text("Exportar Datos")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                expanded = it.isNotBlank()
                            },
                            label = { Text("Buscar atleta") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        if (expanded && filteredAthletes.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .background(MaterialTheme.colors.surface)
                            ) {
                                filteredAthletes.take(5).forEach { athlete ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedAthlete = athlete
                                                searchText = athlete.user.fullname
                                                expanded = false
                                            }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        UserAvatar(athlete.user)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                title = "Fuerza",
                value =
                    StrengthStatistic.calculatePerformance(statistics?.strengthStats ?: emptyList())
                        .formatAsPercentage(),
                description = "Desde que comenzó el entrenamiento",
                icon = Icons.Default.AccountBox,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Resistencia",
                value =
                    EnduranceStatistic.calculatePerformance(statistics?.enduranceStats ?: emptyList())
                        .formatAsPercentage(),
                description = "Mejora en capacidad aeróbica",
                icon = Icons.Default.AccountBox,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Cambios Corporales",
                value =
                    BodyMeasurementHistoric.calculatePerformance(statistics?.measurementHistory ?: emptyList())
                        .formatAsPercentage(),
                description = "Reducción de peso desde el inicio",
                icon = Icons.Default.AccountBox,
                modifier = Modifier.weight(1f)
            )
        }

        ScrollableTabRow(
            selectedTabIndex = when (activeTab) {
                "strength" -> 0
                "endurance" -> 1
                "measurements" -> 2
                else -> 0
            },
            edgePadding = 0.dp,
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.primary
        ) {
            listOf("Fuerza", "Resistencia", "Medidas Corporales")
                .forEachIndexed { index, title ->
                    Tab(
                        selected = when (index) {
                            0 -> activeTab == "strength"
                            1 -> activeTab == "endurance"
                            2 -> activeTab == "measurements"
                            else -> false
                        },
                        onClick = {
                            activeTab = when (index) {
                                0 -> "strength"
                                1 -> "endurance"
                                2 -> "measurements"
                                else -> "strength"
                            }
                        },
                        text = { Text(title) }
                    )
                }
        }

        val rangeComponent: @Composable () -> Unit = {

            var timeRangeExpanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(
                    onClick = { timeRangeExpanded = true }
                ) {
                    Text(
                        when (timeRange) {
                            30.toDuration(DurationUnit.DAYS) -> "Último mes"
                            (30 * 3).toDuration(DurationUnit.DAYS) -> "Últimos 3 meses"
                            (30 * 6).toDuration(DurationUnit.DAYS) -> "Últimos 6 meses"
                            (30 * 12).toDuration(DurationUnit.DAYS) -> "Último año"
                            Duration.INFINITE -> "Todo el historial"
                            else -> "Último mes"
                        }
                    )
                }
                DropdownMenu(
                    expanded = timeRangeExpanded,
                    onDismissRequest = { timeRangeExpanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            timeRange = 30.toDuration(DurationUnit.DAYS)
                            timeRangeExpanded = false
                        }
                    ) { Text("Último mes") }
                    DropdownMenuItem(
                        onClick = {
                            timeRange = (30 * 3).toDuration(DurationUnit.DAYS)
                            timeRangeExpanded = false
                        }
                    ) { Text("Últimos 3 meses") }
                    DropdownMenuItem(
                        onClick = {
                            timeRange = (30 * 6).toDuration(DurationUnit.DAYS)
                            timeRangeExpanded = false
                        }
                    ) { Text("Últimos 6 meses") }
                    DropdownMenuItem(
                        onClick = {
                            timeRange = (30 * 12).toDuration(DurationUnit.DAYS)
                            timeRangeExpanded = false
                        }
                    ) { Text("Último año") }
                    DropdownMenuItem(
                        onClick = {
                            timeRange = Duration.INFINITE
                            timeRangeExpanded = false
                        }
                    ) { Text("Todo el historial") }
                }
            }
        }

        when (activeTab) {
            "strength" -> StrengthEvolutionChart(
                statistics?.strengthChartData ?: StrengthChartData(), rangeComponent = rangeComponent
            )

            "endurance" -> EnduranceEvolutionChart(
                statistics?.enduranceChartData ?: EnduranceChartData(), rangeComponent = rangeComponent
            )

            "measurements" -> BodyMeasurementsChart(
                statistics?.measurementChartData ?: MeasurementChartData(), rangeComponent = rangeComponent
            )
        }

    }
}