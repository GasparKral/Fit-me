package es.gaspardev.controllers

import es.gaspardev.core.domain.entities.User

object LoggedUser {

    var user: User? = null

    fun logOut() {
        this.user = null
    }
}