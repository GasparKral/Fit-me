package es.gaspardev.layout

import Anchor
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.RouterController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SideBarMenuItem(
    text: String,
    path: Route,
    controller: RouterController,
    icon: @Composable () -> Unit?
) {
    Anchor(
        href = path,
        controller,
        modifier = Modifier.fillMaxWidth()
    ) {
        icon.invoke()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            color = if (controller.currentRoute.value == path) MaterialTheme.colors.onPrimary else Color(0xFF5F5F5F)
        )
    }

}