package es.gaspardev.layout.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType

@Composable
fun StatisticsChart() {

    data class ChartData(
        val date: String,
        val workoutCompletion: Int,
        val nutritionAdherence: Int,
        val overallProgress: Int
    )

    val data = listOf(
        ChartData("01/01", 60, 70, 65),
        ChartData("01/08", 70, 80, 75),
        ChartData("01/15", 80, 90, 85),
        ChartData("01/22", 90, 95, 92),
        ChartData("01/29", 95, 98, 96)
    )


    val chartColors = listOf(
        Color(0xFF3B82F6), // Blue 500 - Workout
        Color(0xFF10B981), // Emerald 500 - Nutrition
        Color(0xFF8B5CF6)  // Violet 500 - Overall
    )


    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "Workout Completion",
            data = data.map { it.workoutCompletion.toDouble() }, // [60.0, 70.0, 80.0, 90.0, 95.0]
            lineColor = chartColors[0], // Blue 500
            lineType = LineType.CURVED_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Nutrition Adherence",
            data = data.map { it.nutritionAdherence.toDouble() }, // [70.0, 80.0, 90.0, 95.0, 98.0]
            lineColor = chartColors[1], // Emerald 500
            lineType = LineType.CURVED_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Overall Progress",
            data = data.map { it.overallProgress.toDouble() }, // [65.0, 75.0, 85.0, 92.0, 96.0]
            lineColor = chartColors[2], // Violet 500
            lineType = LineType.CURVED_LINE,
            lineShadow = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        // Chart
        LineChart(
            modifier = Modifier.fillMaxSize(),
            linesParameters = testLineParameters,
            xAxisData = data.map { d -> d.date }.toList(),
            animateChart = true,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W400
            ),
            oneLineChart = false,
            isGrid = true,
            gridOrientation = GridOrientation.HORIZONTAL
        )

        // Legend
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf(
                Pair("Workout Completion", chartColors[0]),
                Pair("Nutrition Adherence", chartColors[1]),
                Pair("Overall Progress", chartColors[2])
            ).forEach { (label, color) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

