package es.gaspardev.layout.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.layout.DialogState
import es.gaspardev.states.LoggedTrainer
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.assign
import fit_me.composeapp.generated.resources.cancel
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AsignDialog(
    plan: @Composable () -> Unit,
    onAcceptAction: (Athlete) -> Unit = {},
) {

    var open by remember { mutableStateOf(false) }
    var selectedAthlete: Athlete? by remember { mutableStateOf(null) }


    Column {
        plan.invoke()
        Spacer(Modifier.height(16.dp))
        ExposedDropdownMenuBox(
            expanded = open,
            onExpandedChange = { open = !open },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedAthlete?.user?.fullname ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text("Diet Type") },
                placeholder = { Text("Select type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = open) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = open,
                onDismissRequest = { open = false }
            ) {
                LoggedTrainer.state.athletes?.sortedBy { it.user.fullname }?.forEach { it ->
                    DropdownMenuItem(
                        onClick = {
                            selectedAthlete = it
                            open = !open
                        }
                    ) {
                        Text(it.user.fullname)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
            ) {
                OutlinedButton(onClick = { DialogState.close() }) {
                    Text(stringResource(Res.string.cancel))
                }

                Button(
                    enabled = if (selectedAthlete != null) true else false,
                    onClick = {
                        if (selectedAthlete != null) {
                            onAcceptAction(selectedAthlete!!)
                            DialogState.close()
                        }
                    }
                ) {
                    Text(stringResource(Res.string.assign))
                }
            }
        }
    }
}