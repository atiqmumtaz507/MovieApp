package com.atiq.neugelb.ui.view.search_movies

import com.atiq.neugelb.data.models.Movie

sealed class SearchAction {
    data class OnTextChanged(val newText: String): SearchAction()
    data class OnTextSearched(val searchText: String): SearchAction()
    data class MovieClicked(val movie: Movie): SearchAction()
}