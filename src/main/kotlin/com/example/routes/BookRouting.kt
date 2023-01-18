package com.example.routes

import com.example.data.dao
import com.example.models.BookRequest
import com.example.models.TextMessage
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
                call.respond(status = HttpStatusCode.OK, TextMessage("No books found"))
            }
        }

        get("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@get call.respond(

                status = HttpStatusCode.BadRequest, TextMessage("Missing id")
            ))
            val book =
                dao.book(id) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound, TextMessage("No book with id $id")
                )
            call.respond(book)
        }

        post {
            val book = call.receive<BookRequest>()
            if (dao.author(book.author) == null) return@post call.respond(
                status = HttpStatusCode.BadRequest, TextMessage("Missing author id")
            )
            val storedBook = dao.addNewBook(book.name, book.author, book.year)
            call.respond(status = HttpStatusCode.Created, TextMessage("Book stored correctly with id = ${storedBook?.id}"))

        }

        put ("{id?}"){
            val id = Integer.parseInt(call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest))
            val newBook = call.receive<BookRequest>()
            if (dao.author(newBook.author) == null) return@put call.respond(
                status = HttpStatusCode.BadRequest, TextMessage("Missing author id")
            )
            if (dao.editBook(id, newBook.name, newBook.author, newBook.year)){
                call.respond(status = HttpStatusCode.Accepted, TextMessage("Book update correctly"))
            } else {
                call.respond(status = HttpStatusCode.NotFound, TextMessage("No book with id $id"))
            }
        }

        delete("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest))
            if (dao.deleteBook(id)) {
                call.respond(status = HttpStatusCode.Accepted, TextMessage("Book removed correctly"))
            } else {
                call.respond(status = HttpStatusCode.NotFound, TextMessage("Not Found"))
            }
        }
    }
}