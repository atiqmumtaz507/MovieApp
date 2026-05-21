package com.atiq.neugelb.ui.view.search_movies

import com.atiq.neugelb.data.models.Movie

data class SearchScreenState(
    val searchText: String = "",
    val searchResult: List<Movie> = listOf(),
    val searchResultCount: Int = -1,
    var isLoading: Boolean = false
)
