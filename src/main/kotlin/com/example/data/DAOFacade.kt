package com.example.data

import com.example.models.Author
import com.example.models.Book

interface DAOFacade {
    suspend fun allAuthors(): List<Author>
    suspend fun author(id: Int): Author?
    suspend fun addNewAuthor(firstName: String, lastName: String, year: Int): Author?
    suspend fun editAuthor(id: Int, firstName: String, lastName: String, year: Int): Boolean
    suspend fun deleteAuthor(id: Int): Boolean
    suspend fun allBooks(): List<Book>
    suspend fun book(id: Int): Book?
    suspend fun addNewBook(name: String, authorId: Int, year: Int): Book?
    suspend fun editBook(id: Int, name: String, authorId: Int, year: Int): Boolean
    suspend fun deleteBook(id: Int): Boolean
}