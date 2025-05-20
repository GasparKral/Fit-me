package es.gaspardev.core.domain.dtos

data class RegisterTrainerData(
    val userName: String,
    val password: String,
    val email: String,
    val specialization: String?,
    val yearsOfExperience: Int
) {

}