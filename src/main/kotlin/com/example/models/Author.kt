package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Author(val id: String, val firstName: String, val lastName: String, val year: String)

val authorStorage = mutableListOf<Author>()