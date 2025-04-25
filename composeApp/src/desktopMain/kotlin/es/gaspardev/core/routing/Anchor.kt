import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.RouterController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun Anchor(
    href: Route,
    controller: RouterController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isSelected = controller.currentRoute.value == href

    Button(
        onClick = { controller.navigateTo(href) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) MaterialTheme.colors.primary else Color.Companion.Transparent
        ),
        shape = RoundedCornerShape(6.dp),
        elevation = null
    ) {
        content()
    }
}

