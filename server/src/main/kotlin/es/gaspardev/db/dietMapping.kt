package es.gaspardev.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DietDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietDAO>(DietTable)

    var name by DietTable.name
    var sportsmanId by DietTable.sportsmanId
    var initialDate by DietTable.initialDate
    var duration by DietTable.duration
    var description by DietTable.description
}