package es.gaspardev.layout.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import es.gaspardev.core.domain.entities.stadistics.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EnduranceEvolutionChart(
    enduranceData: EnduranceChartData,
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
                        text = "Evolución de Resistencia",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mejora en capacidad aeróbica y resistencia",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                rangeComponent.invoke()
            }
            if (enduranceData.vo2MaxData.isNotEmpty() ||
                enduranceData.cardioEnduranceData.isNotEmpty() ||
                enduranceData.distanceCoveredData.isNotEmpty()
            ) {

                val lineParameters = mutableListOf<LineParameters>()

                // VO2 Max data
                if (enduranceData.vo2MaxData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "VO2 Max",
                            data = enduranceData.vo2MaxData.map { it.value },
                            lineColor = Color(0xFF9C27B0),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                // Resistencia cardiovascular
                if (enduranceData.cardioEnduranceData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Resistencia Cardio",
                            data = enduranceData.cardioEnduranceData.map { it.value },
                            lineColor = Color(0xFF00BCD4),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                // Distancia cubierta
                if (enduranceData.distanceCoveredData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Distancia (km)",
                            data = enduranceData.distanceCoveredData.map { it.value },
                            lineColor = Color(0xFFFF9800),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                val firstDataSeries = when {
                    enduranceData.vo2MaxData.isNotEmpty() -> enduranceData.vo2MaxData
                    enduranceData.cardioEnduranceData.isNotEmpty() -> enduranceData.cardioEnduranceData
                    else -> enduranceData.distanceCoveredData
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
                        gridOrientation = GridOrientation.VERTICAL
                    )
                }

                // Leyenda
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (enduranceData.vo2MaxData.isNotEmpty()) {
                        LegendItem("VO2 Max", Color(0xFF9C27B0))
                    }
                    if (enduranceData.cardioEnduranceData.isNotEmpty()) {
                        LegendItem("Resistencia", Color(0xFF00BCD4))
                    }
                    if (enduranceData.distanceCoveredData.isNotEmpty()) {
                        LegendItem("Distancia", Color(0xFFFF9800))
                    }
                }
            } else {
                EmptyChartPlaceholder("No hay datos de resistencia disponibles")
            }
        }
    }
}
