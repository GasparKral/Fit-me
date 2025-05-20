package es.gaspardev.core.domain.dtos

data class TrainerDashBoardInfo(
    val pendingWorkouts: Int = 0,
    val newsSportsman: Int = 0,
    val activePlans: Int = 0,
    val newsPlans: Int = 0,
    val upcommingSessions: Int = 0,
    val unreadMessages: Int = 0,
    val newMessages: Int = 0
)