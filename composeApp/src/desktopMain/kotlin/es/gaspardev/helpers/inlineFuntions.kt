package es.gaspardev.helpers

import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.enums.OpeningMode
import es.gaspardev.layout.DialogState
import es.gaspardev.layout.dialogs.DietDialog
import es.gaspardev.layout.dialogs.WorkoutDialog

inline fun createWorkout(optionalTemplate: WorkoutTemplate? = null, crossinline onAcceptAction: (Workout) -> Unit) {
    DialogState.openWith {
        WorkoutDialog(
            template = optionalTemplate,
            mode = OpeningMode.CREATION
        ) { onAcceptAction(it) }
    }
}

inline fun editWorkout(workout: Workout, crossinline onAcceptAction: (Workout) -> Unit) {
    DialogState.openWith {
        WorkoutDialog(
            workout,
            mode = OpeningMode.EDIT
        ) { onAcceptAction(it) }
    }
}

inline fun visualizeWorkout(workout: Workout, crossinline onAcceptAction: (Workout) -> Unit = {}) {
    DialogState.openWith {
        WorkoutDialog(
            workout,
            mode = OpeningMode.VISUALIZE
        ) { onAcceptAction(it) }
    }
}

inline fun createDiet(diet: Diet, crossinline onAcceptAction: (Diet) -> Unit) {
    DialogState.openWith {
        DietDialog(
            diet,
            mode = OpeningMode.CREATION
        ) { onAcceptAction(it) }
    }
}

inline fun editDiet(diet: Diet, crossinline onAcceptAction: (Diet) -> Unit) {
    DialogState.openWith {
        DietDialog(
            diet,
            mode = OpeningMode.EDIT
        ) { onAcceptAction(it) }
    }
}

inline fun visualizeDiet(diet: Diet, crossinline onAcceptAction: (Diet) -> Unit = {}) {
    DialogState.openWith {
        DietDialog(
            diet,
            mode = OpeningMode.VISUALIZE
        ) { onAcceptAction(it) }
    }
}