package es.gaspardev.core.domain.entities.users.info

import kotlinx.serialization.Serializable

@Serializable
data class Measurements(
    var weight: Double = 0.toDouble(),
    var height: Double = 0.toDouble(),
    var bodyFat: Double = 0.toDouble(),
    var armSize: Double = 0.toDouble(),         //cm
    var chest_backSize: Double = 0.toDouble(),  //cm
    var hipSize: Double = 0.toDouble(),         //cm
    var legSize: Double = 0.toDouble(),         //cm
    var calvesSize: Double = 0.toDouble()       //cm
)
