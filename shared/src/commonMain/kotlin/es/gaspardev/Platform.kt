package es.gaspardev

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform