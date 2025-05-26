package es.gaspardev.db.mappings.notes

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.db.NotesTable
import es.gaspardev.db.mappings.users.UserDao
import es.gaspardev.db.mappings.users.UserEntity
import es.gaspardev.modules.endpoints.BaseDao
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

// Note DAO
class NoteDao() : BaseDao<NoteEntity, Int>(NoteEntity) {
    fun toDomain(note: NoteEntity): Note {
        return Note(
            user = UserDao().toDomain(note.userId),
            message = note.message
        )
    }
}

class NoteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoteEntity>(NotesTable)

    var userId by UserEntity referencedOn NotesTable.userId
    var message by NotesTable.message
}
