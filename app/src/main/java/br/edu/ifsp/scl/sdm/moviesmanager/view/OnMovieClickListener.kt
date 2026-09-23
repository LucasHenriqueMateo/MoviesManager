package br.edu.ifsp.scl.sdm.moviesmanager.view

sealed interface OnMovieClickListener {
    fun onMovieClick(position: Int)
    fun onRemoveMovieMenuItemClick(position: Int)
    fun onEditMovieMenuItemClick(position: Int)
}
