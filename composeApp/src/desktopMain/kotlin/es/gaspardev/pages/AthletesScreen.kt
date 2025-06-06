package es.gaspardev.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import es.gaspardev.core.LocalRouter
import es.gaspardev.core.domain.dtos.QrData
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.layout.athletes.AthleteCard
import es.gaspardev.states.LoggedTrainer
import es.gaspardev.utils.QrGenerator
import es.gaspardev.utils.saveQrToDesktop
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.add_athlete
import fit_me.composeapp.generated.resources.athlete_search_query
import fit_me.composeapp.generated.resources.clients
import fit_me.composeapp.generated.resources.qr_generated_successfully
import fit_me.composeapp.generated.resources.qr_generation_error
import fit_me.composeapp.generated.resources.key_creation_error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource


suspend fun agregateNewSportman(toaster: ToasterState) {
    TrainerRepositoryImp().generateRegistrationKey(LoggedTrainer.state.trainer!!).fold(
        { it ->
            try {
                saveQrToDesktop(
                    QrGenerator.generateQrBitmap(
                        QrData(it),
                        300
                    ),
                    "Clave de Usuario"
                )
                toaster.show(Res.string.qr_generated_successfully, type = ToastType.Success)
            } catch (e: Exception) {
                toaster.show(Res.string.qr_generation_error, it)
            }
        },
        { err -> toaster.show(Res.string.key_creation_error, err.message ?: "") }
    )
}


@Composable
fun AthletesScreen(
    height: Dp
) {
    val scope = rememberCoroutineScope()
    val toaster = rememberToasterState()
    val controller = LocalRouter.current
    val sportsmanList = LoggedTrainer.state.athletes!!
    var searchQuery by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Toaster(toaster, Modifier.align(Alignment.End))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(stringResource(Res.string.clients), style = MaterialTheme.typography.subtitle1)

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                agregateNewSportman(toaster)
                            }
                        },
                        content = { Text(stringResource(Res.string.add_athlete)) }
                    )
                }
                Spacer(Modifier.height(24.dp))
                Row {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(stringResource(Res.string.athlete_search_query)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            modifier = Modifier.heightIn(max = height).fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 275.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sportsmanList.filter { sportsman ->
                sportsman.user.fullname.contains(
                    searchQuery,
                    true
                )
            }) { sportsman ->
                AthleteCard(
                    athlete = sportsman,
                    onClick = { controller.navigateTo(Routes.AthleteInfo.load(sportsman)) }
                )
            }
        }
    }

}
