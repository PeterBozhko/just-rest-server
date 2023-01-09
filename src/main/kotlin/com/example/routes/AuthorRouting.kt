package com.example.routes

import com.example.models.Author
import com.example.models.authorStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

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
            val id = Integer.parseInt(call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            ))
            val author =
                authorStorage.getOrNull(id) ?: return@get call.respondText(
                    "No author with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(author)
        }
        post {
            val author = call.receive<Author>()
            val id = call.parameters.getOrFail("id").toInt()
//            val id = call.parameters.getOrFail("id").toInt()
//            val id = call.parameters.getOrFail("id").toInt()
//            val id = call.parameters.getOrFail("id").toInt()
            authorStorage.add(author)
            call.respondText("Author stored correctly with id = ${authorStorage.size - 1}", status = HttpStatusCode.Created)

        }

       put ("{id?}"){
           val id = Integer.parseInt(call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest))
           val newAuthor = call.receive<Author>()
           authorStorage.getOrNull(id) ?: return@put call.respondText(
               "No author with id $id",
               status = HttpStatusCode.NotFound
           )
           authorStorage[id] = newAuthor
           call.respondText("Author update correctly", status = HttpStatusCode.Accepted)
       }

        delete("{id?}") {
            val id = Integer.parseInt(call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest))
            val author = authorStorage.getOrNull(id) ?: return@delete call.respondText(
                "No author with id $id",
                status = HttpStatusCode.NotFound
            )
            if (authorStorage.remove(author)) {
                call.respondText("Author removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}