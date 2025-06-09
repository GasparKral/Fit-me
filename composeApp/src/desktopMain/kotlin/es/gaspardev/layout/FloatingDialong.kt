package es.gaspardev.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

object DialogState {
    val isOpen = mutableStateOf(false)
    val content = mutableStateOf<(@Composable () -> Unit)?>(null)

    fun openWith(newContent: @Composable () -> Unit) {
        content.value = newContent
        isOpen.value = true
    }

    fun changeContent(newContent: @Composable () -> Unit) {
        content.value = newContent
    }

    fun close() {
        isOpen.value = false
        content.value = null
    }
}

data class BorderStyle(val width: Dp, val color: Color, val shape: Shape)

@Composable
fun FloatingDialog(
    modifier: Modifier = Modifier,
    border: BorderStyle = BorderStyle(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(12.dp)),
    maxSize: Pair<Dp, Dp>,
    onDismiss: () -> Unit = { DialogState.close() }
) {
    val isOpen by DialogState.isOpen
    val currentContent by DialogState.content
    val scrollState = rememberScrollState()

    if (isOpen) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .zIndex(Float.MAX_VALUE)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(maxSize.first.div(2.25f))
                    .height(maxSize.second.div(1.25f))
                    .background(
                        MaterialTheme.colors.background,
                        border.shape
                    )
                    .border(border.width, border.color, border.shape)
                    .clip(border.shape)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .zIndex(1f)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = MaterialTheme.colors.primary
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(top = 40.dp)
                        .verticalScroll(scrollState)
                ) {
                    currentContent?.invoke()
                }

                // Mover el VerticalScrollbar DENTRO del Box del diálogo
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(scrollState),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .padding(end = 4.dp, top = 16.dp, bottom = 16.dp),
                    style = LocalScrollbarStyle.current.copy(
                        hoverColor = MaterialTheme.colors.primaryVariant,
                        unhoverColor = MaterialTheme.colors.primary
                    )
                )
            }
        }
    }
}