package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Author(val id: Int, val firstName: String, val lastName: String, val year: Int)

@Serializable
data class AuthorRequest(val firstName: String, val lastName: String, val year: Int)

object Authors : Table() {
    val id = integer("id").autoIncrement()
    val firstName = varchar("title", 128)
    val lastName = varchar("body", 1024)
    val year = integer("year")

    override val primaryKey = PrimaryKey(id)
}