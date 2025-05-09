package es.gaspardev.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


class SportsmanDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SportsmanDAO>(SportsmanTable)


}