package br.edu.ifsp.scl.sdm.moviesmanager.controller

import br.edu.ifsp.scl.sdm.moviesmanager.model.Movie
import br.edu.ifsp.scl.sdm.moviesmanager.model.MovieDao
import br.edu.ifsp.scl.sdm.moviesmanager.model.MovieOrder
import br.edu.ifsp.scl.sdm.moviesmanager.model.MovieSqlite
import br.edu.ifsp.scl.sdm.moviesmanager.view.MainActivity

class MainController(mainActivity: MainActivity) {
    private val movieDao: MovieDao = MovieSqlite(mainActivity)

    fun insertMovie(movie: Movie) = movieDao.createMovie(movie)
    fun getMovies(order: MovieOrder) = movieDao.retrieveMovies(order)
    fun modifyMovie(movie: Movie) = movieDao.updateMovie(movie)
    fun removeMovie(name: String) = movieDao.deleteMovie(name)
}
