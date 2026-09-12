package br.edu.ifsp.scl.sdm.moviesmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.moviesmanager.R
import br.edu.ifsp.scl.sdm.moviesmanager.databinding.TileMovieBinding
import br.edu.ifsp.scl.sdm.moviesmanager.model.Movie

class MovieRvAdapter(
    private val movieList: MutableList<Movie>
) : RecyclerView.Adapter<MovieRvAdapter.MovieViewHolder>() {
    inner class MovieViewHolder(tileMovieBinding: TileMovieBinding) :
        RecyclerView.ViewHolder(tileMovieBinding.root) {
        val nameTv: TextView = tileMovieBinding.nameTv
        val detailsTv: TextView = tileMovieBinding.detailsTv
        val ratingTv: TextView = tileMovieBinding.ratingTv
    }

    override fun getItemCount() = movieList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TileMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            MovieViewHolder(this)
        }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        movieList[position].also { movie ->
            with(holder) {
                nameTv.text = movie.name
                detailsTv.text = itemView.context.getString(
                    R.string.movie_details_format,
                    movie.releaseYear,
                    movie.genre,
                    movie.duration
                )
                ratingTv.text = movie.rating?.let {
                    itemView.context.getString(R.string.rating_format, it)
                } ?: itemView.context.getString(R.string.no_rating)
            }
        }
    }
}
