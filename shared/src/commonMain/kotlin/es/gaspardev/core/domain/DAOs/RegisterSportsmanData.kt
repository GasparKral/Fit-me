package es.gaspardev.core.domain.DAOs

data class RegisterSportsmanData(
    val userName: String,
    val password: String,
    val email: String,
    val sex: Boolean, // 1 hombre, 0 mujer
    val age: Int,
    val trainerId: Int
) {

}