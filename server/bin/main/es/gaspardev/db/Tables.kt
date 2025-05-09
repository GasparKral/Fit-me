package es.gaspardev.db

import es.gaspardev.enums.BodyPart
import es.gaspardev.enums.MediaType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

object UserTable : IntIdTable("user") {
    val name = varchar("name", 50)
    val password = varchar("password", 250)
    val email = varchar("email", 250)
    val creationTime = timestamp("creationTime")
    val userImage = varchar("image_dir", 100)
}

object SocialLinksTable : IntIdTable("socials") {
    val trainerId = optReference(
        "trainer_id",
        TrainerTable.id,
        ReferenceOption.CASCADE,
        ReferenceOption.CASCADE
    )
    val socialMedia = varchar("media", 25)
    val link = varchar("links", 150)
}

object CertificationTable : IntIdTable("certifications") {
    val name = varchar("name", 50)
    val issuinOrganization = varchar("issuin_organization", 75)
    val optainedDate = timestamp("dateObtained")
}

object TrainerTable : IntIdTable("trainer") {
    val userId = reference(
        "user_id",
        UserTable.id,
        ReferenceOption.CASCADE,
        ReferenceOption.CASCADE
    )
    val specialization = varchar("specialization", 50)
    val years_of_experience = integer("experience")
    val bio = text("bio")
    val rating = double("rating")
    val certification =
        array<Int>("certifications", Int.MAX_VALUE) // guarda la referencia al indice de la tabla Certifications
}

object SportsmanTable : IntIdTable("sportsmans") {
    val userId = reference(
        "user_id",
        UserTable.id,
        ReferenceOption.CASCADE,
        ReferenceOption.CASCADE
    )
    val trainerId =
        optReference(
            "trainer_id",
            TrainerTable.id,
            onDelete = ReferenceOption.SET_NULL,
            onUpdate = ReferenceOption.CASCADE
        )
    val age = integer("age")
    val weight = double("weight").nullable()
    val height = double("height").nullable()
    val sex = bool("sex") // 1 Hombre 0 Mujer
}

object ResourceTable : IntIdTable("resources") {
    val type = customEnumeration(
        "type", "MediaType",
        { value -> MediaType.valueOf(value as String) },
        { PGEnum<MediaType>("MediaType", it) }
    )
    val path = varchar("path", 255)
}

object NoteTable : IntIdTable("notes") {
    val user = reference("user_id", UserTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val message = mediumText("message")
}

object ExercisesTable : IntIdTable("exercises") {
    val name = varchar("name", 200)
    val bodyPart = customEnumeration(
        "body_part", "BodyPart",
        { value -> BodyPart.valueOf(value as String) },
        { PGEnum<BodyPart>("BodyPart", it) }
    )
    val reps = integer("reps")
    val sets = integer("sets")
    val description = mediumText("description")
    val author = optReference("author", TrainerTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
    val video = optReference("video", ResourceTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
}

object ExerciseOptionalLinks : Table("exercise_optional_links") {
    val exercise = reference("exercise_id", ExercisesTable, onDelete = ReferenceOption.CASCADE)
    val optional = reference("optional_id", ExercisesTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(exercise, optional, name = "PK_Exercise_Optional")
}

object ChatTable : IntIdTable("chat") {
    val traine = reference("trainer_id", TrainerTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
    val sportsman = reference("sportsman_id", SportsmanTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
}

object MessagesTable : IntIdTable("messages") {
    val chat = reference("chat_id", ChatTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val user = reference("user_id", UserTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val message = varchar("message_text", 250)
    val messageType = customEnumeration(
        "message_type",
        "MessageType",
        { value -> MediaType.valueOf(value as String) },
        { PGEnum<MediaType>("MediaType", it) })
}

object Workouts : IntIdTable("workouts") {

}