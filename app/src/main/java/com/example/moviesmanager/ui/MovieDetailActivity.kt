package com.example.moviesmanager.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.R
import com.example.moviesmanager.database.DatabaseHelper
import com.example.moviesmanager.model.Movie
import com.google.android.material.appbar.MaterialToolbar

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        dbHelper = DatabaseHelper(this)

        val toolbar: MaterialToolbar = findViewById(R.id.detailToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val etYear: EditText = findViewById(R.id.etYear)
        val etDuration: EditText = findViewById(R.id.etDuration)
        val cbWatched: CheckBox = findViewById(R.id.cbWatched)
        val spGenre: Spinner = findViewById(R.id.spGenre)
        val rbRating: RatingBar = findViewById(R.id.rbRating)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)
        val btnDelete: Button = findViewById(R.id.btnDelete)

        val genres = listOf("Romance", "Aventura", "Terror", "Comédia", "Drama")
        spGenre.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genres)

        val name = intent.getStringExtra("MOVIE_NAME")
        if (name == null) {
            Toast.makeText(this, "Filme inválido.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        movie = dbHelper.getMovieByName(name)
        if (movie == null) {
            Toast.makeText(this, "Filme não encontrado.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        movie?.let {
            supportActionBar?.title = it.name
            etYear.setText(it.year.toString())
            etDuration.setText(it.duration.toString())
            cbWatched.isChecked = it.watched
            rbRating.rating = it.rating ?: 0f
            val idx = genres.indexOf(it.genre)
            spGenre.setSelection(if (idx >= 0) idx else 0)
        }

        btnUpdate.setOnClickListener {
            movie?.apply {
                year = etYear.text.toString().toIntOrNull() ?: 0
                duration = etDuration.text.toString().toIntOrNull() ?: 0
                watched = cbWatched.isChecked
                rating = rbRating.rating
                genre = spGenre.selectedItem.toString()
                if (dbHelper.updateMovie(this)) {
                    Toast.makeText(this@MovieDetailActivity, "Atualizado!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@MovieDetailActivity, "Falha ao atualizar.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnDelete.setOnClickListener {
            movie?.let {
                if (dbHelper.deleteMovie(it.name)) {
                    Toast.makeText(this, "Deletado!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Falha ao deletar.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
