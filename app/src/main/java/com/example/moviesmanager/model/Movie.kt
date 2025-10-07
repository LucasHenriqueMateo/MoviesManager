package com.example.moviesmanager.model

data class Movie(
    val name: String,
    var year: Int,
    var duration: Int,
    var genre: String,
    var watched: Boolean,
    var rating: Float?
)
