package es.gaspardev.layout.sideBar

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
import es.gaspardev.core.Route
import es.gaspardev.core.RouterState
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun SideBarMenuItem(
    text: String,
    path: Route,
    controller: RouterState,
    icon: ImageVector
) {
    Anchor(
        href = path,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, stringResource(Res.string.icon_for, text))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text,
                color = if (controller.currentRoute == path) MaterialTheme.colors.onPrimary else Color(0xFF5F5F5F)
            )
        }

    }
}