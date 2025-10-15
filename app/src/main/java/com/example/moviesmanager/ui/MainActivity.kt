package com.example.moviesmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesmanager.R
import com.example.moviesmanager.adapter.MovieAdapter
import com.example.moviesmanager.database.DatabaseHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView
    private var currentOrder: String? = null // null, "name", "rating DESC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerViewMovies)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MovieAdapter(
            movieList = dbHelper.getAllMovies(currentOrder),
            onItemClick = { movie ->
                val intent = Intent(this, MovieDetailActivity::class.java)
                intent.putExtra("MOVIE_NAME", movie.name)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.fabAdd)
        fab.setOnClickListener {
            startActivity(Intent(this, AddMovieActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        adapter.updateList(dbHelper.getAllMovies(currentOrder))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_name -> {
                currentOrder = "name"
                refreshList()
                return true
            }
            R.id.action_sort_rating -> {
                currentOrder = "rating DESC"
                refreshList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
