package br.edu.ifsp.scl.sdm.moviesmanager.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.scl.sdm.moviesmanager.R

class MovieSqlite(context: Context) : MovieDao {
    companion object {
        private const val MOVIE_DATABASE_FILE = "moviesManager"
        private const val MOVIE_TABLE = "movie"
        private const val NAME_COLUMN = "name"
        private const val RELEASE_YEAR_COLUMN = "release_year"
        private const val STUDIO_COLUMN = "studio"
        private const val DURATION_COLUMN = "duration"
        private const val WATCHED_COLUMN = "watched"
        private const val RATING_COLUMN = "rating"
        private const val GENRE_COLUMN = "genre"

        // O nome e a chave primaria natural do filme (unico e nao editavel).
        // COLLATE NOCASE faz a unicidade, a busca e a ordenacao ignorarem maiusculas/minusculas.
        private const val CREATE_MOVIE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS $MOVIE_TABLE ( " +
                "$NAME_COLUMN TEXT NOT NULL PRIMARY KEY COLLATE NOCASE, " +
                "$RELEASE_YEAR_COLUMN INTEGER NOT NULL, " +
                "$STUDIO_COLUMN TEXT NOT NULL, " +
                "$DURATION_COLUMN INTEGER NOT NULL, " +
                "$WATCHED_COLUMN INTEGER NOT NULL DEFAULT 0, " +
                "$RATING_COLUMN INTEGER CHECK ($RATING_COLUMN IS NULL OR $RATING_COLUMN BETWEEN 0 AND 10), " +
                "$GENRE_COLUMN TEXT NOT NULL );"
    }

    // Criando uma instancia do Sqlite
    private val movieDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        MOVIE_DATABASE_FILE,
        MODE_PRIVATE,
        null
    )

    // Criando a tabela de filmes
    init {
        try {
            movieDatabase.execSQL(CREATE_MOVIE_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.message.toString())
        }
    }

    override fun createMovie(movie: Movie): Long =
        movieDatabase.insert(MOVIE_TABLE, null, movie.toContentValues())

    override fun retrieveMovies(): MutableList<Movie> {
        val movieList: MutableList<Movie> = mutableListOf()
        movieDatabase.query(
            MOVIE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$NAME_COLUMN ASC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                movieList.add(cursor.toMovie())
            }
        }

        return movieList
    }

    override fun updateMovie(movie: Movie): Int = movieDatabase.update(
        MOVIE_TABLE,
        movie.toContentValues(),
        "$NAME_COLUMN = ?",
        arrayOf(movie.name)
    )

    override fun deleteMovie(name: String): Int = movieDatabase.delete(
        MOVIE_TABLE,
        "$NAME_COLUMN = ?",
        arrayOf(name)
    )

    private fun Movie.toContentValues() = ContentValues().apply {
        put(NAME_COLUMN, name)
        put(RELEASE_YEAR_COLUMN, releaseYear)
        put(STUDIO_COLUMN, studio)
        put(DURATION_COLUMN, duration)
        put(WATCHED_COLUMN, if (watched) 1 else 0)
        rating?.let { put(RATING_COLUMN, it) } ?: putNull(RATING_COLUMN)
        put(GENRE_COLUMN, genre)
    }

    private fun Cursor.toMovie() = Movie(
        name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
        releaseYear = getInt(getColumnIndexOrThrow(RELEASE_YEAR_COLUMN)),
        studio = getString(getColumnIndexOrThrow(STUDIO_COLUMN)),
        duration = getInt(getColumnIndexOrThrow(DURATION_COLUMN)),
        watched = getInt(getColumnIndexOrThrow(WATCHED_COLUMN)) == 1,
        rating = getColumnIndexOrThrow(RATING_COLUMN).let { if (isNull(it)) null else getInt(it) },
        genre = getString(getColumnIndexOrThrow(GENRE_COLUMN))
    )
}
