package es.gaspardev.components

import androidx.compose.material.Badge
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DifficultyBadge(difficulty: String) {

    Badge(
        backgroundColor = when (difficulty) {
            "beginner" -> Color(0xFFE8F5E9)
            "intermediate" -> Color(0xFFE3F2FD)
            "advanced" -> Color(0xFFFFEBEE)
            else -> Color(0xFFF5F5F5)
        },
        contentColor = when (difficulty) {
            "beginner" -> Color(0xFF2E7D32)
            "intermediate" -> Color(0xFF1565C0)
            "advanced" -> Color(0xFFC62828)
            else -> Color(0xFF424242)
        }
    ) {
        Text(difficulty.replaceFirstChar { it.uppercase() })
    }
}