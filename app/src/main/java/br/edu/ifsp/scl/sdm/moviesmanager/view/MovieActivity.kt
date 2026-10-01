package br.edu.ifsp.scl.sdm.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.sdm.moviesmanager.R
import br.edu.ifsp.scl.sdm.moviesmanager.databinding.ActivityMovieBinding
import br.edu.ifsp.scl.sdm.moviesmanager.model.Constant.EXTRA_MOVIE
import br.edu.ifsp.scl.sdm.moviesmanager.model.Constant.EXTRA_MOVIE_NAMES
import br.edu.ifsp.scl.sdm.moviesmanager.model.Constant.EXTRA_VIEW_MOVIE
import br.edu.ifsp.scl.sdm.moviesmanager.model.Movie

class MovieActivity : AppCompatActivity() {
    private val amvb: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }

    // Nomes ja cadastrados, usados para garantir que o nome do novo filme seja unico
    private val existingNames: List<String> by lazy {
        intent.getStringArrayListExtra(EXTRA_MOVIE_NAMES) ?: arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(amvb.root)
        ViewCompat.setOnApplyWindowInsetsListener(amvb.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(amvb.toolbarIn.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Spinner de generos
        val genres = resources.getStringArray(R.array.genres)
        amvb.genreSp.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val receivedMovie = IntentCompat.getParcelableExtra(intent, EXTRA_MOVIE, Movie::class.java)
        val viewMovie = intent.getBooleanExtra(EXTRA_VIEW_MOVIE, false)

        supportActionBar?.subtitle = getString(
            when {
                receivedMovie == null -> R.string.new_movie
                viewMovie -> R.string.movie_details
                else -> R.string.edit_movie
            }
        )

        receivedMovie?.let { movie ->
            with(amvb) {
                nameEt.setText(movie.name)
                releaseYearEt.setText(movie.releaseYear.toString())
                studioEt.setText(movie.studio)
                durationEt.setText(movie.duration.toString())
                watchedCb.isChecked = movie.watched
                ratingEt.setText(movie.rating?.toString() ?: "")
                genres.indexOf(movie.genre).also { index ->
                    if (index >= 0) {
                        genreSp.setSelection(index)
                    }
                }

                // O nome identifica o filme e nao pode ser alterado
                nameEt.isEnabled = false

                if (viewMovie) {
                    releaseYearEt.isEnabled = false
                    studioEt.isEnabled = false
                    durationEt.isEnabled = false
                    watchedCb.isEnabled = false
                    ratingEt.isEnabled = false
                    genreSp.isEnabled = false
                    saveBt.visibility = View.GONE
                }
            }
        }

        amvb.saveBt.setOnClickListener {
            if (validateFields(receivedMovie == null)) {
                with(amvb) {
                    val movie = Movie(
                        name = nameEt.text.toString().trim(),
                        releaseYear = releaseYearEt.text.toString().toInt(),
                        studio = studioEt.text.toString().trim(),
                        duration = durationEt.text.toString().toInt(),
                        watched = watchedCb.isChecked,
                        rating = ratingEt.text.toString().trim().toIntOrNull(),
                        genre = genreSp.selectedItem.toString()
                    )
                    setResult(RESULT_OK, Intent().putExtra(EXTRA_MOVIE, movie))
                    finish()
                }
            }
        }
    }

    // Seta de voltar da toolbar: fecha a tela sem salvar, como o voltar do sistema
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun validateFields(newMovie: Boolean): Boolean {
        var valid = true
        with(amvb) {
            val name = nameEt.text.toString().trim()
            if (name.isEmpty()) {
                nameEt.error = getString(R.string.required_field)
                valid = false
            } else if (newMovie && existingNames.any { it.equals(name, ignoreCase = true) }) {
                nameEt.error = getString(R.string.name_already_exists)
                valid = false
            }

            if (releaseYearEt.text.toString().toIntOrNull() == null) {
                releaseYearEt.error = getString(R.string.required_field)
                valid = false
            }

            if (studioEt.text.toString().trim().isEmpty()) {
                studioEt.error = getString(R.string.required_field)
                valid = false
            }

            val duration = durationEt.text.toString().toIntOrNull()
            if (duration == null || duration <= 0) {
                durationEt.error = getString(R.string.invalid_duration)
                valid = false
            }

            val ratingText = ratingEt.text.toString().trim()
            if (ratingText.isNotEmpty()) {
                val rating = ratingText.toIntOrNull()
                if (rating == null || rating !in 0..10) {
                    ratingEt.error = getString(R.string.invalid_rating)
                    valid = false
                }
            }
        }
        return valid
    }
}
