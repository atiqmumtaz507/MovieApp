package com.atiq.neugelb.data.remote

import com.atiq.neugelb.BuildConfig
import com.atiq.neugelb.data.models.GenreResponse
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.models.MoviesResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @Headers("Authorization: Bearer ${BuildConfig.MOVIE_API_ACCESS_TOKEN}")
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "de-DE",
        @Query("page") page: Int = 1,
        @Query("region") region: String = "DE"
    ): MoviesResult

    @Headers("Authorization: Bearer ${BuildConfig.MOVIE_API_ACCESS_TOKEN}")
    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "de-DE"
    ): GenreResponse

    @Headers("Authorization: Bearer ${BuildConfig.MOVIE_API_ACCESS_TOKEN}")
    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "de-DE"
    ): Movie

    @Headers("Authorization: Bearer ${BuildConfig.MOVIE_API_ACCESS_TOKEN}")
    @GET("search/movie")
    suspend fun searchMovieByName(
        @Query("query") query: String,
        @Query("language") language: String = "de-DE"
    ): MoviesResult
}