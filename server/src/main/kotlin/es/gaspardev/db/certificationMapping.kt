package es.gaspardev.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CertificationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CertificationDAO>(CertificationTable)

    var name by CertificationTable.name
    var issuingOrganization by CertificationTable.issuinOrganization
    var obtainedDate by CertificationTable.optainedDate
}