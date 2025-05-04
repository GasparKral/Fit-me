package es.gaspardev.layout

import Anchor
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.RouterController

@Composable
fun SideBarMenuItem(
    text: String,
    path: Route,
    controller: RouterController,
    icon: ImageVector
) {
    Anchor(
        href = path,
        controller,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, "Icon for ${text}")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text,
                color = if (controller.currentRoute.value == path) MaterialTheme.colors.onPrimary else Color(0xFF5F5F5F)
            )
        }

    }
}