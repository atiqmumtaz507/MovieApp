package com.atiq.neugelb.domain.repositories

import com.atiq.neugelb.data.models.GenreResponse
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.models.MoviesResult
import com.atiq.neugelb.data.remote.ApiResult

interface MoviesRepository {
    suspend fun getPopularMovies(page: Int): ApiResult<MoviesResult>
    suspend fun getGenresList(): ApiResult<GenreResponse>

    suspend fun getMovie(id: Long): ApiResult<Movie>

    suspend fun searchMovieByName(query: String): ApiResult<MoviesResult>
}