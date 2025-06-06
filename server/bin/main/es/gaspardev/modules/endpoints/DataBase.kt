package es.gaspardev.modules.endpoints

import es.gaspardev.utils.DATA_BASE_NAME
import es.gaspardev.utils.DATA_BASE_PASSWORD
import es.gaspardev.utils.DATA_BASE_PORT
import es.gaspardev.utils.DATA_BASE_USERNAME
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PGobject

fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:${DATA_BASE_PORT}/${DATA_BASE_NAME}",
        driver = "org.postgresql.Driver",
        user = DATA_BASE_USERNAME,
        password = DATA_BASE_PASSWORD
    )

    transaction {
        // SchemaUtils.create(*DatabaseConfig.allTables)
    }
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}