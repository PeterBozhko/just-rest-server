package com.example.plugins

import com.example.routes.authorRouting
import com.example.routes.bookRouting
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        bookRouting()
        authorRouting()
    }
}
