package es.gaspardev.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import es.gaspardev.layout.BorderStyle

@Composable
fun ProgressBar(
    value: Double,
    showMetrics: Boolean = true,
    label: (@Composable () -> Unit)? = null,
    progressColor: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
    height: Dp = 16.dp,
    borderStyle: BorderStyle = BorderStyle(0.dp, Color.Transparent, RoundedCornerShape(6.dp))
) {
    val clampedValue = value.coerceIn(0.0, 100.0)
    val progressFraction = clampedValue / 100.0

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            label?.invoke()
            if (showMetrics) {
                Text(
                    text = "${clampedValue.toInt()}%",
                    color = MaterialTheme.colors.primary.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(borderStyle.shape)  // Aplica el clip para la forma del borde
                .border(
                    width = borderStyle.width,
                    color = borderStyle.color,
                    shape = borderStyle.shape
                )
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progressFraction.toFloat())
                    .background(progressColor)
                    .clip(borderStyle.shape)
            )
        }
    }
}