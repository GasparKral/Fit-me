package es.gaspardev.modules.endpoints

import es.gaspardev.utils.DATA_BASE_NAME
import es.gaspardev.utils.DATA_BASE_PASSWORD
import es.gaspardev.utils.DATA_BASE_PORT
import es.gaspardev.utils.DATA_BASE_USERNAME
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.vendors.*

fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:${DATA_BASE_PORT}/${DATA_BASE_NAME}",
        driver = "org.postgresql.Driver",
        user = DATA_BASE_USERNAME,
        password = DATA_BASE_PASSWORD,
    )
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

