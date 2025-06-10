package es.gaspardev.helpers

import androidx.compose.runtime.Composable
import es.gaspardev.enums.*
import fit_me.composeapp.generated.resources.*
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.cardio
import fit_me.composeapp.generated.resources.full_body
import fit_me.composeapp.generated.resources.strength
import org.jetbrains.compose.resources.stringResource

fun Double.formatAsPercentage(): String = "%.1f%%".format(this)

val resWeekDay: @Composable (WeekDay) -> String = { day ->
    when (day) {
        WeekDay.MONDAY -> stringResource(Res.string.day_monday)
        WeekDay.THURSDAY -> stringResource(Res.string.day_thursday)
        WeekDay.WEDNESDAY -> stringResource(Res.string.day_wednesday)
        WeekDay.TUESDAY -> stringResource(Res.string.day_tuesday)
        WeekDay.FRIDAY -> stringResource(Res.string.day_friday)
        WeekDay.SATURDAY -> stringResource(Res.string.day_saturday)
        WeekDay.SUNDAY -> stringResource(Res.string.day_sunday)
    }
}

val resWorkoutType: @Composable (WorkoutType) -> String = { type ->
    when (type) {
        WorkoutType.CARDIO -> stringResource(Res.string.cardio)
        WorkoutType.STRENGTH -> stringResource(Res.string.strength)
        WorkoutType.FULL_BODY -> stringResource(Res.string.full_body)
        WorkoutType.UPPER_BODY -> stringResource(Res.string.upper_body)
        WorkoutType.LOWER_BODY -> stringResource(Res.string.lower_body)
        WorkoutType.CORE -> stringResource(Res.string.core)
        WorkoutType.FLEXIBILITY -> stringResource(Res.string.flexibility)
        WorkoutType.ALL -> stringResource(Res.string.all)
    }
}

val resBodyPart: @Composable (BodyPart) -> String = { bodyPart ->
    when (bodyPart) {
        BodyPart.LEG -> stringResource(Res.string.body_part_arm)
        BodyPart.ARM -> stringResource(Res.string.body_part_leg)
        BodyPart.SHOULDER -> stringResource(Res.string.body_part_shoulder)
        BodyPart.CORE -> stringResource(Res.string.body_part_core)
        BodyPart.BACK -> stringResource(Res.string.body_part_back)
        BodyPart.CHEST -> stringResource(Res.string.body_part_chest)
        BodyPart.FULL_BODY -> stringResource(Res.string.full_body)
    }
}

val resDifficulty: @Composable (Difficulty) -> String = { difficulty ->
    when (difficulty) {
        Difficulty.EASY -> stringResource(Res.string.difficulty_beginner)
        Difficulty.ADVANCE -> stringResource(Res.string.difficulty_intermediate)
        Difficulty.HARD -> stringResource(Res.string.difficulty_advanced)
    }
}

val resDietType: @Composable (DietType) -> String = { type ->
    when (type) {
        DietType.WEIGHT_LOSS -> stringResource(Res.string.weight_loss)
        DietType.MUSCLE_GAIN -> stringResource(Res.string.muscle_gain)
        DietType.BALANCED -> stringResource(Res.string.balanced)
        DietType.VEGETARIAN -> stringResource(Res.string.vegetarian)
        DietType.VEGAN -> stringResource(Res.string.vegan)
        DietType.LOW_CARB -> stringResource(Res.string.low_carb)
        DietType.PERFORMANCE -> stringResource(Res.string.performance)
        DietType.ALL -> stringResource(Res.string.all)
    }
}

val resMealType: @Composable (MealType) -> String = { type ->
    when (type) {
        MealType.BREAKFAST -> stringResource(Res.string.meal_breakfast)
        MealType.LUNCH -> stringResource(Res.string.meal_lunch)
        MealType.DINNER -> stringResource(Res.string.meal_dinner)
        MealType.SNACK -> stringResource(Res.string.meal_snack)
        MealType.PRE_WORKOUT -> stringResource(Res.string.meal_pre_workout)
        MealType.POST_WORKOUT -> stringResource(Res.string.meal_post_workout)
    }
}