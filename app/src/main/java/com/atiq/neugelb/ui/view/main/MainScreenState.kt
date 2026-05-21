package com.atiq.neugelb.ui.view.main

import com.atiq.neugelb.data.models.Movie

data class MainScreenState(
    val popularMovies: List<Movie> = listOf(),
    val isLoadingData: Boolean = false,
    val currentPopularMoviePagerPosition: Int = 0,
    val moviesList: List<Movie> = listOf(),
    val genres: Map<String, Int> = mapOf("All Movies" to -1),
    val selectedGenreId: Int = -1,
    val isPagerAutoScrollPaused: Boolean = false
)
