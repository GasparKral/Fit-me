package es.gaspardev.components

import androidx.compose.material.Badge
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import es.gaspardev.enums.Difficulty
import es.gaspardev.helpers.resDifficulty

@Composable
fun DifficultyBadge(difficulty: Difficulty) {

    Badge(
        backgroundColor = when (difficulty) {
            Difficulty.EASY -> Color(0xFFE8F5E9)
            Difficulty.ADVANCE -> Color(0xFFE3F2FD)
            Difficulty.HARD -> Color(0xFFFFEBEE)
            else -> Color(0xFFF5F5F5)
        },
        contentColor = when (difficulty) {
            Difficulty.EASY -> Color(0xFF2E7D32)
            Difficulty.ADVANCE -> Color(0xFF1565C0)
            Difficulty.HARD -> Color(0xFFC62828)
            else -> Color(0xFF424242)
        }
    ) {
        Text(resDifficulty(difficulty), color = MaterialTheme.colors.primary)
    }
}