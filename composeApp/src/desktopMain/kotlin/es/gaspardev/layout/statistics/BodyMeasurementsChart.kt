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
import es.gaspardev.core.domain.entities.stadistics.MeasurementChartData
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun BodyMeasurementsChart(
    measurementData: MeasurementChartData,
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
                        text = "Cambios en Medidas Corporales",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Seguimiento de peso, % grasa corporal y masa muscular",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                rangeComponent.invoke()
            }
            if (measurementData.weightData.isNotEmpty() ||
                measurementData.bodyFatData.isNotEmpty() ||
                measurementData.muscleMassData.isNotEmpty()
            ) {

                val lineParameters = mutableListOf<LineParameters>()

                // Datos de peso
                if (measurementData.weightData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Peso (kg)",
                            data = measurementData.weightData.map { it.value },
                            lineColor = Color(0xFF3F51B5),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                // Datos de grasa corporal
                if (measurementData.bodyFatData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Grasa Corporal (%)",
                            data = measurementData.bodyFatData.map { it.value },
                            lineColor = Color(0xFFE91E63),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                // Datos de masa muscular
                if (measurementData.muscleMassData.isNotEmpty()) {
                    lineParameters.add(
                        LineParameters(
                            label = "Masa Muscular (kg)",
                            data = measurementData.muscleMassData.map { it.value },
                            lineColor = Color(0xFF8BC34A),
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true
                        )
                    )
                }

                val firstDataSeries = when {
                    measurementData.weightData.isNotEmpty() -> measurementData.weightData
                    measurementData.bodyFatData.isNotEmpty() -> measurementData.bodyFatData
                    else -> measurementData.muscleMassData
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
                    if (measurementData.weightData.isNotEmpty()) {
                        LegendItem("Peso", Color(0xFF3F51B5))
                    }
                    if (measurementData.bodyFatData.isNotEmpty()) {
                        LegendItem("Grasa %", Color(0xFFE91E63))
                    }
                    if (measurementData.muscleMassData.isNotEmpty()) {
                        LegendItem("Masa Muscular", Color(0xFF8BC34A))
                    }
                }
            } else {
                EmptyChartPlaceholder("No hay datos de medidas corporales disponibles")
            }
        }
    }
}
