package com.atiq.neugelb.data.repository

import com.atiq.neugelb.data.models.GenreResponse
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.models.MoviesResult
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.data.remote.MoviesApi
import com.atiq.neugelb.domain.repositories.MoviesRepository
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: MoviesApi
) : MoviesRepository {
    private val cache = mutableMapOf<Long, Movie>()

    override suspend fun getPopularMovies(page: Int): ApiResult<MoviesResult> {
        try {
            val popularMovies = api.getPopularMovies(page = page)
            popularMovies.results.forEach {
                cache[it.id] = it
            }
            return ApiResult.Success(popularMovies)
        } catch (e: IOException) {
            return ApiResult.NetworkError(e)
        } catch (e: HttpException) {
            return ApiResult.HttpError(e.code(), e.message())
        } catch (e: Throwable) {
            return ApiResult.UnknownError(e)
        }
    }

    override suspend fun getGenresList(): ApiResult<GenreResponse> {
        try {
            val genres = api.getMovieGenres()
            return ApiResult.Success(genres)
        } catch (e: IOException) {
            return ApiResult.NetworkError(e)
        } catch (e: HttpException) {
            return ApiResult.HttpError(e.code(), e.message())
        } catch (e: Throwable) {
            return ApiResult.UnknownError(e)
        }
    }

    override suspend fun getMovie(id: Long): ApiResult<Movie> {
        try {
            val movie = cache[id] ?: api.getMovieById(id).also {
                cache[id] = it
            }
            return ApiResult.Success(movie)
        } catch (e: IOException) {
            return ApiResult.NetworkError(e)
        } catch (e: HttpException) {
            return ApiResult.HttpError(e.code(), e.message())
        } catch (e: Throwable) {
            return ApiResult.UnknownError(e)
        }
    }

    override suspend fun searchMovieByName(query: String): ApiResult<MoviesResult> {
        try {
            val result = api.searchMovieByName(query = query)
            result.results.forEach {
                cache[it.id] = it
            }
            return ApiResult.Success(result)
        } catch (e: IOException) {
            return ApiResult.NetworkError(e)
        } catch (e: HttpException) {
            return ApiResult.HttpError(e.code(), e.message())
        } catch (e: Throwable) {
            return ApiResult.UnknownError(e)
        }
    }
}