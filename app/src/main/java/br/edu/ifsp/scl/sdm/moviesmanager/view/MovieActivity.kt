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
import br.edu.ifsp.scl.sdm.moviesmanager.model.Constant.EXTRA_VIEW_MOVIE
import br.edu.ifsp.scl.sdm.moviesmanager.model.Movie

class MovieActivity : AppCompatActivity() {
    private val amvb: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
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
            if (receivedMovie == null) R.string.new_movie else R.string.movie_details
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
            with(amvb) {
                val movie = Movie(
                    name = nameEt.text.toString().trim(),
                    releaseYear = releaseYearEt.text.toString().toIntOrNull() ?: 0,
                    studio = studioEt.text.toString().trim(),
                    duration = durationEt.text.toString().toIntOrNull() ?: 0,
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
