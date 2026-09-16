package br.edu.ifsp.scl.sdm.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.sdm.moviesmanager.R
import br.edu.ifsp.scl.sdm.moviesmanager.databinding.ActivityMovieBinding
import br.edu.ifsp.scl.sdm.moviesmanager.model.Constant.EXTRA_MOVIE
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
        supportActionBar?.subtitle = getString(R.string.new_movie)

        // Spinner de generos
        amvb.genreSp.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
