package com.example.routes

import com.example.data.dao
import com.example.models.AuthorRequest
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
                call.respondText("No authors found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            ))
            val author = dao.author(id) ?: return@get call.respondText(
                    "No author with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(author)
        }
        post {
            val author = call.receive<AuthorRequest>()
            val storedAuthor = dao.addNewAuthor(author.firstName, author.lastName, author.year)
            call.respondText("Author stored correctly with id = ${storedAuthor?.id}", status = HttpStatusCode.Created)

        }

       put ("{id?}"){
           val id = Integer.parseInt(call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest))
           val newAuthor = call.receive<AuthorRequest>()
           if (dao.editAuthor(id, newAuthor.firstName, newAuthor.lastName, newAuthor.year)){
               call.respondText("Author update correctly", status = HttpStatusCode.Accepted)
           } else {
               call.respondText("No author with id $id", status = HttpStatusCode.NotFound)
           }
       }

        delete("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest))
            if (dao.deleteAuthor(id)) {
                call.respondText("Author removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("No author with id $id", status = HttpStatusCode.NotFound)
            }
        }
    }
}