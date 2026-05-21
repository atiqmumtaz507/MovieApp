package com.atiq.neugelb.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResult(
    val page: Int,
    val results: List<Movie>,
    @SerialName("total_pages")
    val totalPages: Int
)

@Serializable
data class Movie(
    val adult: Boolean,
    @SerialName("genre_ids")
    val genreIds: List<Int>?,
    var genreNames: MutableList<String> = mutableListOf(),
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    val id: Long,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0
) {
    fun getImageUrl(type: ImageType): String {
        return "https://image.tmdb.org/t/p/${type.size}$posterPath"
    }

    fun getbackimg(type: ImageType): String {
        return "https://image.tmdb.org/t/p/${type.size}$backdropPath"
    }
    fun getUserPercentage(): Double {
        return (voteAverage / 10.0f) * 100.0f
    }
}

enum class ImageType(val size: String) {
    THUMBNAIL("w92"),
    SMALL_CARDS("w154"),
    POSTER("w342"),
    HIGH_QUALITY("w500"),
    FULL_RESOLUTION("original")
}
