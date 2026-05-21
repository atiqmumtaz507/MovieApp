package com.atiq.neugelb.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val genres: List<Genre>
) {
    fun getGenreName(id: Int): String? {
        val genre = genres.firstOrNull {
            it.id == id
        }
        return genre?.name
    }
}

@Serializable
data class Genre(
    val id: Int,
    val name: String
)