package br.edu.ifsp.scl.sdm.moviesmanager.view

sealed interface OnMovieClickListener {
    fun onMovieClick(position: Int)
}
