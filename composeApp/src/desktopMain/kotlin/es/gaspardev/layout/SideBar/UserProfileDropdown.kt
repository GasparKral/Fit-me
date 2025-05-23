package es.gaspardev.layout.sideBar

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.core.RouterState
import es.gaspardev.pages.Routes
import es.gaspardev.states.LoggedTrainer

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserProfileDropdown(
    controller: RouterState,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                // Avatar
                /*Image(
                    painter = painterResource(Res.drawable.Home),
                    contentDescription = "Trainer",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )*/

                Spacer(modifier = Modifier.width(8.dp))

                // User info
                Column {
                    Text(
                        text = LoggedTrainer.state.trainer!!.user.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(255.dp)
            ) {
                DropdownMenuItem(
                    onClick = { controller.navigateTo(Routes.Settings) },
                    enabled = false
                ) {
                    Text("Mi Perfil y Opciones")
                }

                Divider()

                DropdownMenuItem(
                    onClick = {
                        controller.navigateTo(Routes.Settings)
                        expanded = false
                    }
                ) {
                    Text("Profile")
                }

                DropdownMenuItem(
                    onClick = { expanded = false }
                ) { Text("Subscription") }

                DropdownMenuItem(
                    onClick = { expanded = false }
                ) { Text("Help Center") }

                Divider()

                DropdownMenuItem(
                    onClick = {
                        onLogout()
                        expanded = false
                    },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp).padding(end = 8.dp)
                        )
                        Text("Log out")
                    }
                }
            }
        }
    }
}