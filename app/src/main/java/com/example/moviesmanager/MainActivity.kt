package com.example.moviesmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesmanager.adapter.MovieAdapter
import com.example.moviesmanager.database.DatabaseHelper
import com.example.moviesmanager.model.Movie
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewMovies)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MovieAdapter(
            movieList = dbHelper.getAllMovies(),
            onItemClick = { movie ->
                val intent = Intent(this, com.example.moviesmanager.ui.MovieDetailActivity::class.java)
                intent.putExtra("MOVIE_NAME", movie.name)
                startActivity(intent)
            },
            onItemDelete = { movie ->
                AlertDialog.Builder(this)
                    .setTitle("Excluir filme")
                    .setMessage("Deseja excluir \"${movie.name}\"?")
                    .setPositiveButton("Excluir") { _, _ ->
                        dbHelper.deleteMovie(movie.name)
                        refreshList()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )
        recyclerView.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.fabAdd)
        fab.setOnClickListener {
            startActivity(Intent(this, com.example.moviesmanager.ui.AddMovieActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        adapter.updateList(dbHelper.getAllMovies())
    }
}
