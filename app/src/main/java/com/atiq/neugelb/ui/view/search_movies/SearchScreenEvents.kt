package com.atiq.neugelb.ui.view.search_movies

sealed class SearchScreenEvents {
    data class ShowSnackBarMessage(val message: String): SearchScreenEvents()
}
