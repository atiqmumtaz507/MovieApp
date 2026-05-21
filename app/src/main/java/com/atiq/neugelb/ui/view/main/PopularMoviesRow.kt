package com.atiq.neugelb.ui.view.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.atiq.neugelb.R
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.ui.view.common.CategoryHeadline
import com.atiq.neugelb.ui.view.common.VerticalSpacer

@Composable
fun PopularMoviesRow(
    screenState: MainScreenState,
    pagerPlayTogggleClicked: () -> Unit,
    onItemClicked: (Movie) -> Unit,
    onItemShown: (Int) -> Unit
) {
    Column {

        CategoryHeadline(stringResource(R.string.popular_movies_headline))

        VerticalSpacer()

        val pager = popularMoviesItem(
            screenState = screenState,
            totalMovies = screenState.popularMovies.size,
            pagerPlayTogggleClicked= pagerPlayTogggleClicked,
            onItemClicked = onItemClicked
        ) { index ->
            screenState.popularMovies.get(index)
        }

        LaunchedEffect(
            screenState.currentPopularMoviePagerPosition,
            screenState.popularMovies.size
        ) {
            val target = screenState.currentPopularMoviePagerPosition.coerceIn(
                    0,
                    screenState.popularMovies.size
                )
            if (pager.currentPage != target) {
                pager.animateScrollToPage(target)
            }
        }

        LaunchedEffect(pager.currentPage) {
            onItemShown(pager.currentPage)
        }
    }
}