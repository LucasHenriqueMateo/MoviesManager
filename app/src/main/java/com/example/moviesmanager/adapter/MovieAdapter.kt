package com.example.moviesmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesmanager.R
import com.example.moviesmanager.model.Movie
import java.text.DecimalFormat

class MovieAdapter(
    private var movieList: List<Movie>,
    private val onItemClick: (Movie) -> Unit,
    private val onItemDelete: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val df = DecimalFormat("#.#")

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.tvName.text = movie.name
        holder.tvRating.text = movie.rating?.let { df.format(it) } ?: "N/A"

        holder.itemView.setOnClickListener { onItemClick(movie) }
        holder.btnDelete.setOnClickListener { onItemDelete(movie) }
    }

    override fun getItemCount(): Int = movieList.size

    fun updateList(newList: List<Movie>) {
        movieList = newList
        notifyDataSetChanged()
    }
}
