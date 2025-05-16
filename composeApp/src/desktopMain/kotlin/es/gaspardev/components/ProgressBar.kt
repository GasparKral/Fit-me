package es.gaspardev.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(
    value: Double,
    showMetrics: Boolean = true,
    label: (@Composable () -> Unit)? = null,
    progressColor: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
    height: Dp = 24.dp
) {

    val clampedValue = value.coerceIn(0.0, 100.0)
    val progressFraction = clampedValue / 100.0

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (label != null) {
            label()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progressFraction.toFloat())
                    .background(progressColor)
            )
        }

        if (showMetrics) {
            Spacer(Modifier.height(4.dp))
            Text(text = "${clampedValue.toInt()}%")
        }
    }
}
