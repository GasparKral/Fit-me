package es.gaspardev.database.entities

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.database.Notes
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class NoteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoteEntity>(Notes)

    var userEntity by UserEntity referencedOn Notes.userId
    var content by Notes.content
    var createdAt by Notes.createdAt

    fun toModel(): Note {
        return Note(
            id = this.id.value,
            user = this.userEntity.toModel(),
            content = this.content,
            createdAt = this.createdAt
        )
    }
}