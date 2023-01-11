package com.example.data

import com.example.data.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToAuthor(row: ResultRow) = Author(
        id = row[Authors.id],
        firstName = row[Authors.firstName],
        lastName = row[Authors.lastName],
        year = row[Authors.year]
    )

    private fun resultRowToBook(row: ResultRow) = Book(
        id = row[Books.id],
        name = row[Books.name],
        author = resultRowToAuthor(row),
        year = row[Books.year]
    )

    private fun resultRowToBookWithoutAuthor(row: ResultRow) = Book(
        id = row[Books.id],
        name = row[Books.name],
        author = null,
        year = row[Books.year]
    )

    override suspend fun allAuthors(): List<Author> = dbQuery {
        Authors.selectAll().map(::resultRowToAuthor)
    }

    override suspend fun author(id: Int): Author? = dbQuery {
        Authors.select{ Authors.id eq id }
            .map(::resultRowToAuthor)
            .singleOrNull()
    }

    override suspend fun addNewAuthor(firstName: String, lastName: String, year: Int): Author? = dbQuery {
        val insertStatement = Authors.insert {
            it[Authors.firstName] = firstName
            it[Authors.lastName] = lastName
            it[Authors.year] = year
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAuthor)
    }

    override suspend fun editAuthor(id: Int, firstName: String, lastName: String, year: Int): Boolean = dbQuery {
        Authors.update({ Authors.id eq id }) {
            it[Authors.firstName] = firstName
            it[Authors.lastName] = lastName
            it[Authors.year] = year
        } > 0
    }

    override suspend fun deleteAuthor(id: Int): Boolean = dbQuery {
        Books.deleteWhere { author eq id }
        Authors.deleteWhere { Authors.id eq id } > 0
    }

    override suspend fun allBooks(): List<Book> = dbQuery {
        (Books leftJoin Authors).selectAll().map(::resultRowToBook)
    }

    override suspend fun book(id: Int): Book? = dbQuery {
        (Books leftJoin Authors).select{ Books.id eq id }
            .map(::resultRowToBook)
            .singleOrNull()
    }

    override suspend fun addNewBook(name: String, authorId: Int, year: Int): Book? = dbQuery {
        val insertStatement = Books.insert {
            it[Books.name] = name
            it[author] = authorId
            it[Books.year] = year
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBookWithoutAuthor)
    }

    override suspend fun editBook(id: Int, name: String, authorId: Int, year: Int): Boolean = dbQuery {
        Books.update({ Books.id eq id }) {
            it[Books.name] = name
            it[author] = authorId
            it[Books.year] = year
        } > 0
    }

    override suspend fun deleteBook(id: Int): Boolean = dbQuery {
        Books.deleteWhere { Books.id eq id } > 0
    }
}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allAuthors().isEmpty()) {
            addNewAuthor("John", "Cambridge", 1976)
        }
        if(allBooks().isEmpty()) {
            addNewBook("LOTR", allAuthors().first().id, 1996)
        }
    }
}