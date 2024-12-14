package es.gaspardev.core.domain.entities

data class Sportman(
    val user: User,
    var trainner: Trainner,
    val workouts: List<Workout>,
    var age: Int,
    var weight: Double,
    var height: Double,
    val sex: Boolean,
    val allergies: List<String>
) {

}