package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.DAOs.RegisterSportsmanData
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.db.SportsmanTable
import es.gaspardev.db.UserTable
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId

fun Application.sportsman() {

   routing {
      post(Sportsman.URLPATH + "/create") {
         val newSportsman = call.receive<RegisterSportsmanData>()

         suspendTransaction {

            val userId = UserTable.insertAndGetId { }

            SportsmanTable.insert {
               it[this.userId] = userId
               it[this.trainerId] = newSportsman.trainerId
               it[this.age] = newSportsman.age
               it[this.weight] = null
               it[this.height] = null
               it[this.sex] = newSportsman.sex
            }
         }
      }
   }

}
