package com.example.routes

import com.example.data.dao
import com.example.models.AuthorRequest
import com.example.models.TextMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authorRouting() {
    route("/author") {
        get {
            if (dao.allAuthors().isNotEmpty()) {
                call.respond(dao.allAuthors())
            } else {
                call.respond(status = HttpStatusCode.OK, TextMessage("No authors found"))
            }
        }
        get("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest, TextMessage("Missing id")
            ))
            val author = dao.author(id) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound, TextMessage("No author with id $id")
                )
            call.respond(author)
        }
        post {
            val author = call.receive<AuthorRequest>()
            val storedAuthor = dao.addNewAuthor(author.firstName, author.lastName, author.year)
            call.respond(status = HttpStatusCode.Created, TextMessage("Author stored correctly with id = ${storedAuthor?.id}"))

        }

       put ("{id?}"){
           val id = Integer.parseInt(call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest))
           val newAuthor = call.receive<AuthorRequest>()
           if (dao.editAuthor(id, newAuthor.firstName, newAuthor.lastName, newAuthor.year)){
               call.respond(status = HttpStatusCode.Accepted, TextMessage("Author update correctly"))
           } else {
               call.respond(status = HttpStatusCode.NotFound, TextMessage("No author with id $id"))
           }
       }

        delete("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest))
            if (dao.deleteAuthor(id)) {
                call.respond(status = HttpStatusCode.Accepted, TextMessage("Author removed correctly"))
            } else {
                call.respond(status = HttpStatusCode.NotFound, TextMessage("No author with id $id"))
            }
        }
    }
}