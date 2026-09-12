package br.edu.ifsp.scl.sdm.moviesmanager.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.moviesmanager.R
import br.edu.ifsp.scl.sdm.moviesmanager.adapter.MovieRvAdapter
import br.edu.ifsp.scl.sdm.moviesmanager.controller.MainController
import br.edu.ifsp.scl.sdm.moviesmanager.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.moviesmanager.model.Movie

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val movieList: MutableList<Movie> = mutableListOf()

    // Adapter
    private val movieAdapter: MovieRvAdapter by lazy {
        MovieRvAdapter(movieList)
    }

    // Controller
    private val mainController: MainController by lazy {
        MainController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(amb.root)
        ViewCompat.setOnApplyWindowInsetsListener(amb.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.movie_list)

        amb.moviesRv.adapter = movieAdapter
        amb.moviesRv.layoutManager = LinearLayoutManager(this)

        fillMovieList()
    }

    private fun fillMovieList() {
        Thread {
            val movies = mainController.getMovies()
            runOnUiThread {
                movieList.clear()
                movieList.addAll(movies)
                movieAdapter.notifyDataSetChanged()
            }
        }.start()
    }
}
