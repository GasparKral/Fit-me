package es.gaspardev.layout.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import es.gaspardev.components.ToastManager
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.usecases.delete.DeleteTrainerAccount
import es.gaspardev.core.domain.usecases.update.settings.ChangePassword
import es.gaspardev.core.domain.usecases.update.settings.ChangePasswordParams
import es.gaspardev.pages.Routes
import es.gaspardev.states.LoggedTrainer
import kotlinx.coroutines.launch

@Composable
fun AccountSettings() {

    val scope = rememberCoroutineScope()
    val router = LocalRouter.current
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cambiar contraseña",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Actualiza tu contraseña para mantener tu cuenta segura",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text("Contraseña") }
                )

                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text("Repetir contraseña") }
                )

                Button(
                    onClick = {
                        scope.launch {
                            ChangePassword().run(
                                ChangePasswordParams(
                                    LoggedTrainer.state.trainer!!.user,
                                    LoggedTrainer.state.trainer!!.user.getPassword(),
                                    newPassword,
                                    confirmNewPassword
                                )
                            ).fold(
                                { _ -> ToastManager.showSuccess("Contraseña actualizada correctamente") },
                                { err -> ToastManager.showError(err.message!!) }
                            )
                        }
                    }
                ) {
                    Text("Cambiar Contraseña")
                }
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Eliminar cuenta",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "Elimina permanentemente tu cuenta y todos tus datos",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        scope.launch {
                            DeleteTrainerAccount().run(LoggedTrainer.state.trainer!!).fold(
                                { _ ->
                                    LoggedTrainer.logout()
                                    router.navigateTo(Routes.Login)
                                },
                                { err -> ToastManager.showError(err.message!!) }
                            )
                        }
                    }
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Eliminar Cuenta")
                }
            }
        }
    }
}