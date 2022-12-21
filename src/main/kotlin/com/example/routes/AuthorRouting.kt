package com.example.routes

import com.example.models.Author
import com.example.models.authorStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authorRouting() {
    route("/author") {
        get {
            if (authorStorage.isNotEmpty()) {
                call.respond(authorStorage)
            } else {
                call.respondText("No authors found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val book =
                authorStorage.find { it.id == id } ?: return@get call.respondText(
                    "No author with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(book)
        }
        post {
            val author = call.receive<Author>()
            authorStorage.add(author)
            call.respondText("Author stored correctly", status = HttpStatusCode.Created)

        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (authorStorage.removeIf { it.id == id }) {
                call.respondText("Author removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}