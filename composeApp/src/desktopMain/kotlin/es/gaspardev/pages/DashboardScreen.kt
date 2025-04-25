package es.gaspardev.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.gaspardev.controllers.LoggedUser
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.RouterController
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import fit_me.composeapp.generated.resources.*
import fit_me.composeapp.generated.resources.Home
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.Weights
import fit_me.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardScreen(controller: RouterController) {

    val buttons: Map<String, @Composable () -> Unit> = mapOf(
        "Agregar Atleta" to {
            Button(
                onClick = {
                    controller.navigateToWithAcction(
                        Route.Athletes,
                        AddNewAthlete,
                        emptyArray()
                    )
                }) { Text("Agregar Atleta") }
        },
        "Agendar Calendario" to {
            Button(onClick = {
                controller.navigateToWithAcction(
                    Route.Calendar,
                    SetNewEvent,
                    emptyArray()
                )
            }) { Text("Agendar Calendario") }
        }
    )



    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary)
    ) {
        Row {
            Column {
                Text(stringResource(Res.string.app_name) + LoggedUser.user.name)
                Text("Tienes " + TrainerRepositoryImp().getPendingWorkouts(LoggedUser.user) + " atletas esperando rutinas")
            }
            Button(
                onClick = {}
            ) {
                "+ " + stringResource(Res.string.app_name)
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Acciones Rápidas ")
            Text("Tareas que tal vez necesites")
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {}) {
                    Text("Agregar Deportista")
                    Icon(painterResource(Res.drawable.Athlets), "Home Icon")
                }
                Button(onClick = {}) {
                    Text("Crear rutina")
                    Icon(painterResource(Res.drawable.Weights), "Home Icon")
                }
                Button(onClick = {}) {
                    Text("Crear dieta")
                    Icon(painterResource(Res.drawable.Nutrition), "Home Icon")
                }
                Button(onClick = {}) {
                    Text("Agendar Entrenamiento")
                    Icon(painterResource(Res.drawable.Calendar), "Home Icon")
                }
                Button(onClick = {}) {
                    Text("Enviar Mensage")
                    Icon(painterResource(Res.drawable.Messages), "Home Icon")
                }
            }
        }
    }
}