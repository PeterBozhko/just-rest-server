package com.example.routes

import com.example.models.Book
import com.example.models.bookStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bookRouting() {
    route("/book") {
        get {
            if (bookStorage.isNotEmpty()) {
                call.respond(bookStorage)
            } else {
                call.respondText("No books found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val book =
                bookStorage.find { it.id == id } ?: return@get call.respondText(
                    "No book with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(book)
        }
        post {
            val book = call.receive<Book>()
            bookStorage.add(book)
            call.respondText("Book stored correctly", status = HttpStatusCode.Created)

        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (bookStorage.removeIf { it.id == id }) {
                call.respondText("Book removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}