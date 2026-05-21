package com.atiq.neugelb.ui.view.movie_details

import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.ui.view.main.MainScreenAction

sealed class MovieDetailScreenAction {
    object BookMarkClicked: MovieDetailScreenAction()
}