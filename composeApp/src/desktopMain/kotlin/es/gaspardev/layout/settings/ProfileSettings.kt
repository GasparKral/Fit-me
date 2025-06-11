package es.gaspardev.layout.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.gaspardev.core.domain.dtos.settings.ProfileSettingsData
import es.gaspardev.core.domain.usecases.update.settings.UpdateProfileSettings
import es.gaspardev.states.LoggedTrainer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun ProfileSettings() {
    val trainer = LoggedTrainer.state.trainer!!
    var name by remember { mutableStateOf(trainer.user.fullname) }
    var email by remember { mutableStateOf(trainer.user.email) }
    var phone by remember { mutableStateOf(trainer.user.phone) }
    var specialization by remember { mutableStateOf(trainer.specialization) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Información del Perfil",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Actualiza tu información personal y profesional",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre completo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (phone != null) {
                            OutlinedTextField(
                                value = phone!!,
                                onValueChange = { phone = it },
                                label = { Text("Teléfono") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        OutlinedTextField(
                            value = specialization,
                            onValueChange = { specialization = it },
                            label = { Text("Especialización") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        isLoading = true
                        MainScope().launch {
                            UpdateProfileSettings().run(
                                Pair(
                                    LoggedTrainer.state.trainer!!.user,
                                    ProfileSettingsData(
                                        fullName = name,
                                        email = email,
                                        phone = phone,
                                        specialization = specialization,
                                        yearsOfExperience = LoggedTrainer.state.trainer!!.yearsOfExperiencie
                                    )
                                )
                            )
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colors.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Guardar cambios")
                }
            }
        }
    }
}
