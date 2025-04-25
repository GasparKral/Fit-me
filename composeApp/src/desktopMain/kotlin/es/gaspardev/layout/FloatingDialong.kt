package es.gaspardev.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.gaspardev.SCREEN_HEIGHT
import es.gaspardev.SCREEN_WIDTH
import fit_me.composeapp.generated.resources.Home
import fit_me.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

@Composable
fun FloatingDialog(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
): (Boolean) -> Unit {

    val open = remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize() // Llena toda la pantalla
            .wrapContentSize(Alignment.Center) // Centra el Box
            .padding(32.dp)
    ) {
        // Fondo oscuro detrás del diálogo
        if (open.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // Cubre toda la pantalla
                    .background(Color(0x80000000)) // Color negro con opacidad (0x80)
                    .blur(4.dp, BlurredEdgeTreatment.Unbounded)
            )
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background, RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFD9D9D9))
                .width(SCREEN_WIDTH / 3)
                .height(SCREEN_HEIGHT / 2)
        ) {
            Button(
                onClick = { open.value = !open.value },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(painterResource(Res.drawable.Home), "Home Icon", tint = MaterialTheme.colors.onBackground)
            }
            content()
        }
    }

    return { value: Boolean ->
        open.value = value
    }
}
