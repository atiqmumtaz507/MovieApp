package com.atiq.neugelb.ui.view.main

import com.atiq.neugelb.data.models.Movie

sealed class MainScreenAction {
    data class PopularMovieClicked(val movie: Movie): MainScreenAction()
    data class MovieListItemPopulated(val index: Int): MainScreenAction()

    data class MovieFilterApplied(val filterId: Int): MainScreenAction()
    data class UpdateManualPagerPosition(val position: Int): MainScreenAction()
    object PagerPlayPauseToggleClicked: MainScreenAction()
}