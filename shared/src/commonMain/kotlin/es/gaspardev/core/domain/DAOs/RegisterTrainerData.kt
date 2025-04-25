package es.gaspardev.core.domain.DAOs

data class RegisterTrainerData(
    val userName: String,
    val password: String,
    val email: String,
    val specialization: String?,
    val yearsOfExperience: Int
) {

}