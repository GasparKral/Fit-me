package es.gaspardev.ui.screens.athlete

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun WorkoutDetailScreen(
    navigator: DestinationsNavigator
) {
    WorkoutDetailScreenContent()
}


@Composable
fun WorkoutDetailScreenContent(
    onNavigateBack: () -> Unit = {},
    onExerciseClick: (Int) -> Unit = {}
) {

}