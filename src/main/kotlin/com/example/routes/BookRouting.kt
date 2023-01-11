package com.example.routes

import com.example.data.dao
import com.example.models.BookRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bookRouting() {
    route("/book") {
        get {
            if (dao.allBooks().isNotEmpty()) {
                call.respond(dao.allBooks())
            } else {
                call.respondText("No books found", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            ))
            val book =
                dao.book(id) ?: return@get call.respondText(
                    "No book with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(book)
        }

        post {
            val book = call.receive<BookRequest>()
            if (dao.author(book.author) == null) return@post call.respondText(
                "Missing author id",
                status = HttpStatusCode.BadRequest
            )
            val storedBook = dao.addNewBook(book.name, book.author, book.year)
            call.respondText("Book stored correctly with id = ${storedBook?.id}", status = HttpStatusCode.Created)

        }

        put ("{id?}"){
            val id = Integer.parseInt(call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest))
            val newBook = call.receive<BookRequest>()
            if (dao.author(newBook.author) == null) return@put call.respondText(
                "Missing author id",
                status = HttpStatusCode.BadRequest
            )
            if (dao.editBook(id, newBook.name, newBook.author, newBook.year)){
                call.respondText("Book update correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("No book with id $id", status = HttpStatusCode.NotFound)
            }
        }

        delete("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest))
            if (dao.deleteBook(id)) {
                call.respondText("Book removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}