package com.example.moviesmanager.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.R
import com.example.moviesmanager.database.DatabaseHelper
import com.example.moviesmanager.model.Movie

class AddMovieActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        dbHelper = DatabaseHelper(this)

        val etName: EditText = findViewById(R.id.etName)
        val etYear: EditText = findViewById(R.id.etYear)
        val etDuration: EditText = findViewById(R.id.etDuration)
        val spinnerGenre: Spinner = findViewById(R.id.spinnerGenre)
        val cbWatched: CheckBox = findViewById(R.id.cbWatched)
        val btnSave: Button = findViewById(R.id.btnSave)

        val genres = listOf("Romance", "Aventura", "Terror", "Comédia", "Drama")
        spinnerGenre.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genres)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val year = etYear.text.toString().toIntOrNull() ?: 0
            val duration = etDuration.text.toString().toIntOrNull() ?: 0
            val genre = spinnerGenre.selectedItem.toString()
            val watched = cbWatched.isChecked

            if (name.isEmpty()) {
                Toast.makeText(this, "Informe o nome do filme!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val movie = Movie(name, year, duration, genre, watched, null)
            val result = dbHelper.addMovie(movie)

            if (result) {
                Toast.makeText(this, "Filme adicionado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao adicionar filme (nome duplicado?)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
