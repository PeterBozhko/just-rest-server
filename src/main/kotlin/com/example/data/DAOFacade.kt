package com.example.data

import com.example.models.Author

interface DAOFacade {
    suspend fun allAuthors(): List<Author>
    suspend fun author(id: Int): Author?
    suspend fun addNewAuthor(firstName: String, lastName: String, year: Int): Author?
    suspend fun editAuthor(id: Int, firstName: String, lastName: String, year: Int): Boolean
    suspend fun deleteAuthor(id: Int): Boolean
}