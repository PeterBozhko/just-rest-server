package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class BookRequest(val name: String, val author: Int, val year: Int)

@Serializable
data class Book(val id: Int, val name: String, val author: Author?, val year: Int)


object Books : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 1024)
    val author = integer("authorId").references(Authors.id)
    val year = integer("year")

    override val primaryKey = PrimaryKey(id)
}