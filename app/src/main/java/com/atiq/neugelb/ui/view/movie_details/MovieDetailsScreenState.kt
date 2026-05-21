package com.atiq.neugelb.ui.view.movie_details

import com.atiq.neugelb.data.models.Movie

data class MovieDetailsScreenState(
    val movie: Movie? = null,
    val isLoading: Boolean = false
)
