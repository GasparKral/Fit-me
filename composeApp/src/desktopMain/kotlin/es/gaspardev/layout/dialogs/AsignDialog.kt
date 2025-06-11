package es.gaspardev.layout.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.gaspardev.components.AutoCompleteTextField
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.states.LoggedTrainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun AsignDialog(
    plan: @Composable () -> Unit,
    onAcceptAction: (Athlete) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var selectedAthlete: Athlete? by remember { mutableStateOf(null) }
    val athletes = LoggedTrainer.state.athletes ?: emptyList()


    Column(
        modifier = Modifier.padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header with icon and title
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Asignar Plan",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )

            Text(
                text = "Selecciona un atleta para asignar este plan",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }

        // Plan preview card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f)),
            elevation = 0.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                plan.invoke()
            }
        }

        // Athlete selection section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colors.primary
                )
                Text(
                    text = "Buscar atleta",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
            }

            if (athletes.isEmpty()) {
                // Empty state
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "No hay atletas disponibles",
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Agrega atletas para poder asignar planes",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                AutoCompleteTextField(
                    options = athletes,
                    getFilterableText = { athlete -> athlete.user.fullname },
                    onItemSelected = { athlete -> selectedAthlete = athlete },
                    dropDownComponent = { athlete ->
                        UserAvatar(user = athlete.user)
                    },
                    placeHolder = { Text("Buscar por nombre del atleta...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                )
            }
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            OutlinedButton(
                onClick = { onCancel() },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                ),
                border = BorderStroke(
                    1.5.dp,
                    MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    "Cancelar",
                    style = MaterialTheme.typography.button,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = {
                    selectedAthlete?.let { athlete ->
                        onAcceptAction(athlete)
                        MainScope().launch {
                            LoggedTrainer.updateAthleteInfo(athlete.user.id)
                        }
                    }
                },
                enabled = selectedAthlete != null && athletes.isNotEmpty(),
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.38f)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Asignar Plan",
                        style = MaterialTheme.typography.button,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

}