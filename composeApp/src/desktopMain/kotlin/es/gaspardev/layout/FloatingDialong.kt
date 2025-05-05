package es.gaspardev.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import es.gaspardev.SCREEN_HEIGHT
import es.gaspardev.SCREEN_WIDTH


object DialogState {
    val isOpen = mutableStateOf(false)
    var content: @Composable () -> Unit = { Box(Modifier) }

    fun open() {
        isOpen.value = true
    }

    fun openWith(content: @Composable () -> Unit) {
        isOpen.value = true
        this.content = content
    }

    fun close() {
        isOpen.value = false
    }
}

data class BorderStyle(val width: Dp, val color: Color, val shape: Shape)

@Composable
fun FloatingDialog(
    modifier: Modifier = Modifier,
    border: BorderStyle = BorderStyle(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(12.dp)),
    onDismiss: () -> Unit = { DialogState.close() },
    content: @Composable () -> Unit = { DialogState.content() }
) {
    if (DialogState.isOpen.value) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .zIndex(Float.MAX_VALUE)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .blur(4.dp, BlurredEdgeTreatment.Unbounded)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(MaterialTheme.colors.background, border.shape)
                    .border(border.width, border.color, border.shape)
                    .width(SCREEN_WIDTH / 3)
                    .height(SCREEN_HEIGHT / 2)
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Go Back",
                        tint = MaterialTheme.colors.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    content()
                }
            }
        }
    }
}
