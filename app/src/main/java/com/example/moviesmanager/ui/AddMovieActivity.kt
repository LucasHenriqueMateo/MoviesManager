package com.example.moviesmanager.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.R
import com.example.moviesmanager.database.DatabaseHelper
import com.example.moviesmanager.model.Movie

class AddMovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        val dbHelper = DatabaseHelper(this)

        val etName: EditText = findViewById(R.id.etName)
        val etYear: EditText = findViewById(R.id.etYear)
        val etDuration: EditText = findViewById(R.id.etDuration)
        val cbWatched: CheckBox = findViewById(R.id.cbWatched)
        val spGenre: Spinner = findViewById(R.id.spGenre)
        val rbRating: RatingBar = findViewById(R.id.rbRating)
        val btnSave: Button = findViewById(R.id.btnSave)

        val genres = listOf("Romance", "Aventura", "Terror", "Comédia", "Drama")
        spGenre.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genres)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Informe um nome (único).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val movie = Movie(
                name = name,
                year = etYear.text.toString().toIntOrNull() ?: 0,
                studio = "", // removido do app
                duration = etDuration.text.toString().toIntOrNull() ?: 0,
                watched = cbWatched.isChecked,
                rating = rbRating.rating,
                genre = spGenre.selectedItem.toString()
            )

            if (dbHelper.addMovie(movie)) {
                Toast.makeText(this, "Filme adicionado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro: nome já existe.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
