package es.gaspardev.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import es.gaspardev.layout.statistics.StatCard
import es.gaspardev.layout.statistics.StatisticCard


@Composable
fun StatisticsScreen() {
    var selectedAthlete by remember { mutableStateOf(sampleAthletes[0].id) }
    var timeRange by remember { mutableStateOf("6months") }
    var activeTab by remember { mutableStateOf("strength") }

    val athlete = sampleAthletes.find { it.id == selectedAthlete } ?: sampleAthletes[0]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
                Button(
                    onClick = { /* Handle export */ },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onSurface,
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Icon(Icons.Default.AccountBox, contentDescription = "Export")
                    Spacer(Modifier.width(8.dp))
                    Text("Exportar Datos")
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = athlete.name,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        /*sampleAthletes.forEach { athlete ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Avatar(
                                            painter = painterResource(id = R.drawable.placeholder),
                                            contentDescription = "Avatar",
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Column {
                                            Text(athlete.name)
                                            Text(
                                                text = athlete.status,
                                                color = when (athlete.status) {
                                                    "active" -> Color.Green
                                                    else -> MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                                },
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    selectedAthlete = athlete.id
                                    expanded = false
                                }
                            )
                        }*/
                    }
                }

                Spacer(Modifier.width(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var timeRangeExpanded by remember { mutableStateOf(false) }
                    Box {
                        /* OutlinedTextField(
                             value = when (timeRange) {
                                 "1month" -> "Último mes"
                                 "3months" -> "Últimos 3 meses"
                                 "6months" -> "Últimos 6 meses"
                                 "1year" -> "Último año"
                                 "all" -> "Todo el historial"
                                 else -> "Periodo de tiempo"
                             },
                             onValueChange = {},
                             readOnly = true,
                             modifier = Modifier.width(180.dp),
                             onClick = { timeRangeExpanded = true }
                         )*/
                        DropdownMenu(
                            expanded = timeRangeExpanded,
                            onDismissRequest = { timeRangeExpanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    timeRange = "1month"
                                    timeRangeExpanded = false
                                }
                            ) { Text("Último mes") }
                            DropdownMenuItem(
                                onClick = {
                                    timeRange = "3months"
                                    timeRangeExpanded = false
                                }
                            ) { Text("Últimos 3 meses") }
                            DropdownMenuItem(
                                onClick = {
                                    timeRange = "6months"
                                    timeRangeExpanded = false
                                }
                            ) { Text("Últimos 6 meses") }
                            DropdownMenuItem(
                                onClick = {
                                    timeRange = "1year"
                                    timeRangeExpanded = false
                                }
                            ) { Text("Último año") }
                            DropdownMenuItem(
                                onClick = {
                                    timeRange = "all"
                                    timeRangeExpanded = false
                                }
                            ) { Text("Todo el historial") }
                        }
                    }

                    Button(
                        onClick = { /* Handle filter */ },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Icon(Icons.Default.AccountBox, contentDescription = "Filter")
                        Spacer(Modifier.width(8.dp))
                        Text("Filtros")
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = "Fuerza",
                    value = "+28%",
                    description = "Desde que comenzó el entrenamiento",
                    icon = Icons.Default.AccountBox,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Resistencia",
                    value = "+35%",
                    description = "Mejora en capacidad aeróbica",
                    icon = Icons.Default.AccountBox,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Cambios Corporales",
                    value = "-8.5 kg",
                    description = "Reducción de peso desde el inicio",
                    icon = Icons.Default.AccountBox,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            ScrollableTabRow(
                selectedTabIndex = when (activeTab) {
                    "strength" -> 0
                    "endurance" -> 1
                    "measurements" -> 2
                    "history" -> 3
                    else -> 0
                },
                edgePadding = 0.dp,
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.primary
            ) {
                listOf("Fuerza", "Resistencia", "Medidas Corporales", "Historial Completo")
                    .forEachIndexed { index, title ->
                        Tab(
                            selected = when (index) {
                                0 -> activeTab == "strength"
                                1 -> activeTab == "endurance"
                                2 -> activeTab == "measurements"
                                3 -> activeTab == "history"
                                else -> false
                            },
                            onClick = {
                                activeTab = when (index) {
                                    0 -> "strength"
                                    1 -> "endurance"
                                    2 -> "measurements"
                                    3 -> "history"
                                    else -> "strength"
                                }
                            },
                            text = { Text(title) }
                        )
                    }
            }
        }

        item {
            when (activeTab) {
                "strength" -> StatisticCard(
                    title = "Evolución de Fuerza",
                    description = "Progreso en levantamientos principales a lo largo del tiempo",
                    content = { /* Strength chart would go here */ }
                )

                "endurance" -> StatisticCard(
                    title = "Evolución de Resistencia",
                    description = "Mejora en capacidad aeróbica y resistencia",
                    content = { /* Endurance chart would go here */ }
                )

                "measurements" -> StatisticCard(
                    title = "Cambios en Medidas Corporales",
                    description = "Seguimiento de peso, % grasa corporal y medidas",
                    content = { /* Measurements chart would go here */ }
                )

                "history" -> StatisticCard(
                    title = "Historial Completo de Progreso",
                    description = "Registro detallado de todas las métricas y evaluaciones",
                    content = { /* History table would go here */ }
                )
            }
        }
    }
}
