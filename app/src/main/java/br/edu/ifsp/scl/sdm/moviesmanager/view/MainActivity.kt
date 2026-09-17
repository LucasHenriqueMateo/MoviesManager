package br.edu.ifsp.scl.sdm.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.moviesmanager.R
import br.edu.ifsp.scl.sdm.moviesmanager.adapter.MovieRvAdapter
import br.edu.ifsp.scl.sdm.moviesmanager.controller.MainController
import br.edu.ifsp.scl.sdm.moviesmanager.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.moviesmanager.model.Constant.EXTRA_MOVIE
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

    private lateinit var marl: ActivityResultLauncher<Intent>

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

        marl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val movie = result.data?.let {
                    IntentCompat.getParcelableExtra(it, EXTRA_MOVIE, Movie::class.java)
                }
                movie?.let { receivedMovie ->
                    if (mainController.insertMovie(receivedMovie) == -1L) {
                        Toast.makeText(this, getString(R.string.movie_not_saved), Toast.LENGTH_SHORT).show()
                    }
                    fillMovieList()
                }
            }
        }

        amb.moviesRv.adapter = movieAdapter
        amb.moviesRv.layoutManager = LinearLayoutManager(this)

        fillMovieList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addMovieMi -> {
                marl.launch(Intent(this, MovieActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
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
