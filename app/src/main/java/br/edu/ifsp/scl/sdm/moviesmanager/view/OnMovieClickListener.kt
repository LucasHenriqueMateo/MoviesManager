package br.edu.ifsp.scl.sdm.moviesmanager.view

sealed interface OnMovieClickListener {
    fun onMovieClick(position: Int)
    fun onRemoveMovieClick(position: Int)
    fun onEditMovieClick(position: Int)
}
