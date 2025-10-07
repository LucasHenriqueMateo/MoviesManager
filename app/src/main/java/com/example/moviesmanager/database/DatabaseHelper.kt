package com.example.moviesmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.moviesmanager.model.Movie

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "movies.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE movies(" +
                    "name TEXT PRIMARY KEY," +
                    "year INTEGER," +
                    "duration INTEGER," +
                    "genre TEXT," +
                    "watched INTEGER," +
                    "rating REAL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS movies")
        onCreate(db)
    }

    fun addMovie(movie: Movie): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", movie.name)
            put("year", movie.year)
            put("duration", movie.duration)
            put("genre", movie.genre)
            put("watched", if (movie.watched) 1 else 0)
            put("rating", movie.rating)
        }
        val result = db.insert("movies", null, values)
        db.close()
        return result != -1L
    }

    fun getAllMovies(orderBy: String? = null): List<Movie> {
        val db = readableDatabase
        val list = mutableListOf<Movie>()
        val cursor = db.query("movies", null, null, null, null, null, orderBy)
        while (cursor.moveToNext()) {
            val movie = Movie(
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getInt(cursor.getColumnIndexOrThrow("year")),
                cursor.getInt(cursor.getColumnIndexOrThrow("duration")),
                cursor.getString(cursor.getColumnIndexOrThrow("genre")),
                cursor.getInt(cursor.getColumnIndexOrThrow("watched")) == 1,
                cursor.getFloat(cursor.getColumnIndexOrThrow("rating"))
            )
            list.add(movie)
        }
        cursor.close()
        db.close()
        return list
    }

    fun getMovieByName(name: String): Movie? {
        val db = readableDatabase
        val cursor = db.query("movies", null, "name=?", arrayOf(name), null, null, null)
        var movie: Movie? = null
        if (cursor.moveToFirst()) {
            movie = Movie(
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getInt(cursor.getColumnIndexOrThrow("year")),
                cursor.getInt(cursor.getColumnIndexOrThrow("duration")),
                cursor.getString(cursor.getColumnIndexOrThrow("genre")),
                cursor.getInt(cursor.getColumnIndexOrThrow("watched")) == 1,
                cursor.getFloat(cursor.getColumnIndexOrThrow("rating"))
            )
        }
        cursor.close()
        db.close()
        return movie
    }

    fun updateMovie(movie: Movie): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("year", movie.year)
            put("duration", movie.duration)
            put("genre", movie.genre)
            put("watched", if (movie.watched) 1 else 0)
            put("rating", movie.rating)
        }
        val result = db.update("movies", values, "name=?", arrayOf(movie.name))
        db.close()
        return result > 0
    }

    fun deleteMovie(name: String): Boolean {
        val db = writableDatabase
        val result = db.delete("movies", "name=?", arrayOf(name))
        db.close()
        return result > 0
    }
}
