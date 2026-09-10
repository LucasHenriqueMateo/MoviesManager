package br.edu.ifsp.scl.sdm.moviesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val name: String = "",
    val releaseYear: Int = 0,
    val studio: String = "",
    val duration: Int = 0,
    val watched: Boolean = false,
    val rating: Int? = null,
    val genre: String = ""
) : Parcelable
