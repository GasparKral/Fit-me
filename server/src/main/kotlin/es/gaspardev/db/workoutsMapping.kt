package es.gaspardev.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WorkoutDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutDAO>(WorkoutsTable)

    var name by WorkoutsTable.name
    var description by WorkoutsTable.description
    var sportsman by WorkoutsTable.sportsman
    var author by WorkoutsTable.author
    var requiresAssistance by WorkoutsTable.requiresAssistance
    var creationDate by WorkoutsTable.creationDate
    var durationWeeks by WorkoutsTable.durationWeeks
}