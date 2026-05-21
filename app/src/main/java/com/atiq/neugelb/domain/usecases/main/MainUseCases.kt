package com.atiq.neugelb.domain.usecases.main

import javax.inject.Inject

data class MainUseCases @Inject constructor(
    val getPopularMovies: GetPopularMoviesUseCase,
    val getGenresList: GetGenresUseCase
)
