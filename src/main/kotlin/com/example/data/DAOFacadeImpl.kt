package com.example.data

import com.example.data.DatabaseFactory.dbQuery
import com.example.models.Author
import com.example.models.Authors
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

    override suspend fun editAuthor(id: Int, firstName: String, lastName: String, year: Int): Boolean  = dbQuery {
        Authors.update({ Authors.id eq id }) {
            it[Authors.firstName] = firstName
            it[Authors.lastName] = lastName
            it[Authors.year] = year
        } > 0
    }

    override suspend fun deleteAuthor(id: Int): Boolean = dbQuery {
        Authors.deleteWhere { Authors.id eq id } > 0
    }
}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allAuthors().isEmpty()) {
            addNewAuthor("John", "Cambrige", 1876)
        }
    }
}