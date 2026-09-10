package br.edu.ifsp.scl.sdm.moviesmanager.model

interface MovieDao {
    fun createMovie(movie: Movie): Long
    fun retrieveMovies(): MutableList<Movie>
    fun updateMovie(movie: Movie): Int
    fun deleteMovie(name: String): Int
}
