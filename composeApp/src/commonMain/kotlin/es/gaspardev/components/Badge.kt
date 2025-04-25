package es.gaspardev.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun Badge(
    text: String?,
    color: Color = MaterialTheme.colors.primary,
    cornerShape: RoundedCornerShape = RoundedCornerShape(12.dp),
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit?
) {

    return Box(
        modifier = modifier.padding(horizontal = if (text != null) 6.dp else 4.dp, vertical = 4.dp)
            .background(color, cornerShape)
    ) {
        icon.invoke()
        text?.let { Text(it) }
    }
}