package com.example.moviesmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.moviesmanager.model.Movie

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "movies.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_MOVIES = "movies"

        private const val COL_NAME = "name"
        private const val COL_YEAR = "year"
        private const val COL_STUDIO = "studio"
        private const val COL_DURATION = "duration"
        private const val COL_WATCHED = "watched"
        private const val COL_RATING = "rating"
        private const val COL_GENRE = "genre"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_MOVIES (
                $COL_NAME TEXT PRIMARY KEY,
                $COL_YEAR INTEGER,
                $COL_STUDIO TEXT,
                $COL_DURATION INTEGER,
                $COL_WATCHED INTEGER,
                $COL_RATING REAL,
                $COL_GENRE TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIES")
        onCreate(db)
    }

    fun addMovie(movie: Movie): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, movie.name)
            put(COL_YEAR, movie.year)
            put(COL_STUDIO, movie.studio)
            put(COL_DURATION, movie.duration)
            put(COL_WATCHED, if (movie.watched) 1 else 0)
            put(COL_RATING, movie.rating)
            put(COL_GENRE, movie.genre)
        }
        val result = db.insert(TABLE_MOVIES, null, values)
        db.close()
        return result != -1L
    }

    fun updateMovie(movie: Movie): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_YEAR, movie.year)
            put(COL_STUDIO, movie.studio)
            put(COL_DURATION, movie.duration)
            put(COL_WATCHED, if (movie.watched) 1 else 0)
            put(COL_RATING, movie.rating)
            put(COL_GENRE, movie.genre)
        }
        val result = db.update(TABLE_MOVIES, values, "$COL_NAME=?", arrayOf(movie.name))
        db.close()
        return result > 0
    }

    fun deleteMovie(name: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_MOVIES, "$COL_NAME=?", arrayOf(name))
        db.close()
        return result > 0
    }

    fun getAllMovies(orderBy: String? = null): List<Movie> {
        val db = readableDatabase
        val movieList = mutableListOf<Movie>()
        val orderClause = if (orderBy != null) "ORDER BY $orderBy" else ""
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MOVIES $orderClause", null)

        if (cursor.moveToFirst()) {
            do {
                val movie = Movie(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDIO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_DURATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_WATCHED)) == 1,
                    if (cursor.isNull(cursor.getColumnIndexOrThrow(COL_RATING))) null
                    else cursor.getFloat(cursor.getColumnIndexOrThrow(COL_RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE))
                )
                movieList.add(movie)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return movieList
    }

    fun getMovieByName(name: String): Movie? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MOVIES WHERE $COL_NAME=?", arrayOf(name))
        var movie: Movie? = null

        if (cursor.moveToFirst()) {
            movie = Movie(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDIO)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_DURATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_WATCHED)) == 1,
                if (cursor.isNull(cursor.getColumnIndexOrThrow(COL_RATING))) null
                else cursor.getFloat(cursor.getColumnIndexOrThrow(COL_RATING)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE))
            )
        }
        cursor.close()
        db.close()
        return movie
    }
}
