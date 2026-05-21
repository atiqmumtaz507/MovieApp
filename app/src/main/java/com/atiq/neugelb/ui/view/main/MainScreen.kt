package com.atiq.neugelb.ui.view.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.atiq.neugelb.R
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.ui.navigation.Screens
import com.atiq.neugelb.ui.view.common.CategoryHeadline
import com.atiq.neugelb.ui.view.common.MovieListItem
import com.atiq.neugelb.ui.view.common.VerticalSpacer
import com.atiq.neugelb.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun MainScreenContainer(viewModel: MainViewModel = hiltViewModel(), controller: NavHostController) {
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbarController.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            println("Event received.")
            when (event) {
                is MainScreenEvents.ShowSnackBarMessage -> {
                    launch {
                        snackbar.show(event.message)
                    }
                }
            }
        }
    }

    MainScreen(screenState,pagerPlayTogggleClicked = {
        viewModel.onAction(MainScreenAction.PagerPlayPauseToggleClicked)
    }, onItemClicked = { movie ->
        viewModel.onAction(MainScreenAction.PopularMovieClicked(movie))
        controller.navigate(Screens.Detail.route + "/${movie.id}")
    }, onMovieListItemShown = { index ->
        viewModel.onAction(MainScreenAction.MovieListItemPopulated(index))
    }, onSearchButtonClicked = {
        controller.navigate(Screens.Search.route)
    }, onFilterItemClick = { filterId ->
        println("Selected filterId $filterId")
        viewModel.onAction(MainScreenAction.MovieFilterApplied(filterId))
    }) { newPosition ->
        viewModel.onAction(MainScreenAction.UpdateManualPagerPosition(newPosition))
    }
}

@Composable
fun MainScreen(
    state: State<MainScreenState>,
    pagerPlayTogggleClicked: () -> Unit,
    onItemClicked: (Movie) -> Unit,
    onMovieListItemShown: (Int) -> Unit,
    onSearchButtonClicked: () -> Unit,
    onFilterItemClick: (Int) -> Unit,
    onItemShown: (Int) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        val listState = rememberLazyListState()
        var fabMode by remember { mutableStateOf(FabMode.BottomEndCircle) }
        var scrollToBottom by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(listState) {
            var prevIndex = listState.firstVisibleItemIndex
            var prevOffset = listState.firstVisibleItemScrollOffset
            snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
                .distinctUntilChanged()
                .collectLatest { (index, offset) ->
                    val scrollingTowardBottom =
                        (index > prevIndex) || (index == prevIndex && offset > prevOffset)
                    val scrollingTowardTop =
                        (index < prevIndex) || (index == prevIndex && offset < prevOffset)

                    println("offset $listState")
                    scrollToBottom = scrollingTowardBottom

                    fabMode = when {
                        scrollingTowardTop -> FabMode.BottomEndCircle
                        scrollingTowardBottom -> FabMode.BottomCenterCapsule
                        else -> fabMode
                    }

                    prevIndex = index
                    prevOffset = offset
                }
        }

        val moviesItems = remember(state.value.moviesList, state.value.selectedGenreId) {
            if (state.value.selectedGenreId == -1) state.value.moviesList
            else state.value.moviesList.filter {
                it.genreIds?.contains(state.value.selectedGenreId) ?: false
            }
        }

        LazyColumn(state = listState) {
            item {
                PopularMoviesRow(state.value, pagerPlayTogggleClicked, onItemClicked, onItemShown)
                VerticalSpacer()
                FiltersRow(Modifier, state.value, onFilterItemClick)
                VerticalSpacer()
                CategoryHeadline(stringResource(R.string.all_movies_headline))
                VerticalSpacer()
            }


            itemsIndexed(moviesItems) { index, movie ->
                onMovieListItemShown(index)
                MovieListItem(movie) {
                    println("${it.title} movie clicked.")
                    onItemClicked(it)
                }
            }
        }

        val center = (LocalConfiguration.current.screenWidthDp.dp.value / 2) - 80
        val offset by animateDpAsState(
            if (fabMode == FabMode.BottomEndCircle)
                0.dp else (-center).dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        AnimatedContent(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = offset),
            targetState = fabMode,
            transitionSpec = {
                fadeIn(tween(120)) togetherWith
                        fadeOut(tween(120))
            },
            label = "fabContent"
        ) { mode ->
            when (mode) {
                FabMode.BottomCenterCapsule -> {
                    TextFab(onSearchButtonClicked)
                }

                FabMode.BottomEndCircle -> {
                    CircleFab(onSearchButtonClicked)
                }
            }
        }

        val showScrollToTopFab by remember {
            derivedStateOf { listState.firstVisibleItemIndex > 1 }
        }

        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                scrollToBottom && showScrollToTopFab,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 160.dp)
                    .padding(end = 10.dp),
                enter = fadeIn(
                    animationSpec = tween(600, easing = LinearOutSlowInEasing)
                ),
                exit = fadeOut(
                    animationSpec = tween(600, easing = LinearOutSlowInEasing)
                )
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    shape = CutCornerShape(35),
                    containerColor = Color.Red.copy(alpha = 0.85f),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Arrow Up")
                }
            }
        }
    }
}

@Composable
fun CircleFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color.Red.copy(alpha = 0.75f),
        contentColor = Color.White
    ) {
        Icon(Icons.Default.Search, contentDescription = "Search")
    }
}

@Composable
fun TextFab(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Red.copy(alpha = 0.75f),
        contentColor = Color.White,
        shape = RoundedCornerShape(999.dp),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Text(stringResource(R.string.fab_search), style = MaterialTheme.typography.labelLarge)
        }
    }
}