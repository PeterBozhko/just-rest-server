package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Book(val id: String, val name: String, val authors: List<Long>, val year: Int)

val bookStorage = mutableListOf<Book>()