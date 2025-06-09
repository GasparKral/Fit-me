package es.gaspardev.layout.sideBar

import Anchor
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
    icon: ImageVector,
    isCollapsed: Boolean = false
) {
    Anchor(
        href = path,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (controller.currentRoute == path)
                MaterialTheme.colors.primary
            else
                Color.Transparent
        )
    ) {

        if (isCollapsed) {
            // Vista colapsada: solo mostrar el ícono centrado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = text,
                    tint = if (controller.currentRoute == path)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            // Vista expandida: mostrar ícono y texto
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    icon,
                    contentDescription = stringResource(Res.string.icon_for, text),
                    tint = if (controller.currentRoute == path)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text,
                    color = if (controller.currentRoute == path)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.primary
                )
            }
        }
    }
}