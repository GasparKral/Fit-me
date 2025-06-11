package es.gaspardev.layout.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import es.gaspardev.core.domain.entities.stadistics.StrengthChartData
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StrengthEvolutionChart(
    strengthData: StrengthChartData,
    modifier: Modifier = Modifier,
    rangeComponent: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        text = "Evolución de Fuerza",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Progreso en levantamientos principales",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                rangeComponent.invoke()
            }

            if (strengthData.benchPressData.isNotEmpty() ||
                strengthData.squatData.isNotEmpty() ||
                strengthData.deadliftData.isNotEmpty()
            ) {

                val lineParameters = mutableListOf<LineParameters>()
                val xAxisLabels = mutableListOf<String>()

                // Preparar datos del press de banca
                if (strengthData.benchPressData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Press Banca",
                            data = strengthData.benchPressData.map { it.value },
                            lineColor = Color(0xFF2196F3),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                // Preparar datos de sentadillas
                if (strengthData.squatData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Sentadillas",
                            data = strengthData.squatData.map { it.value },
                            lineColor = Color(0xFF4CAF50),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                // Preparar datos de peso muerto
                if (strengthData.deadliftData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Peso Muerto",
                            data = strengthData.deadliftData.map { it.value },
                            lineColor = Color(0xFFFF5722),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                // Usar las fechas de la primera serie de datos disponible
                val firstDataSeries = when {
                    strengthData.benchPressData.isNotEmpty() -> strengthData.benchPressData
                    strengthData.squatData.isNotEmpty() -> strengthData.squatData
                    else -> strengthData.deadliftData
                }

                val dateFormatter = SimpleDateFormat("dd/MM", Locale.getDefault())
                val xAxisData = firstDataSeries.map {
                    dateFormatter.format(Date(it.date.toEpochMilliseconds()))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    LineChart(
                        modifier = Modifier.fillMaxSize(),
                        linesParameters = lineParameters,
                        isGrid = true,
                        gridColor = Color.LightGray,
                        xAxisData = xAxisData,
                        animateChart = false,
                        showGridWithSpacer = true,
                        yAxisStyle = TextStyle(
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        ),
                        xAxisStyle = TextStyle(
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.W400
                        ),
                        yAxisRange = 10,
                        oneLineChart = false,
                        gridOrientation = GridOrientation.VERTICAL,
                        descriptionStyle = MaterialTheme.typography.caption
                    )
                }

                // Leyenda
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (strengthData.benchPressData.isNotEmpty()) {
                        LegendItem("Press Banca", Color(0xFF2196F3))
                    }
                    if (strengthData.squatData.isNotEmpty()) {
                        LegendItem("Sentadillas", Color(0xFF4CAF50))
                    }
                    if (strengthData.deadliftData.isNotEmpty()) {
                        LegendItem("Peso Muerto", Color(0xFFFF5722))
                    }
                }
            } else {
                EmptyChartPlaceholder("No hay datos de fuerza disponibles")
            }
        }
    }
}