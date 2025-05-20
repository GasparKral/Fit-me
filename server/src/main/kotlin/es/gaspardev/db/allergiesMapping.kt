package es.gaspardev.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AllergyDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AllergyDAO>(AllergyTable)

    var name by AllergyTable.name
}