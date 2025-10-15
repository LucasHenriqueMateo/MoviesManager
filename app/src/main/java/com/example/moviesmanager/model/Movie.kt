package com.example.moviesmanager.model

data class Movie(
    val name: String,
    var year: Int,
    var studio: String,
    var duration: Int,
    var watched: Boolean,
    var rating: Float?,
    var genre: String
)
