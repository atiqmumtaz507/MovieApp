package com.atiq.neugelb.ui.view.search_movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.atiq.neugelb.R
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.ui.navigation.Screens
import com.atiq.neugelb.ui.view.common.SearchBar
import com.atiq.neugelb.ui.view.common.SearchResultItem
import com.atiq.neugelb.ui.view.main.LocalSnackbarController
import com.atiq.neugelb.viewmodels.SearchMovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SearchMovieScreenContainer(
    viewModel: SearchMovieViewModel = hiltViewModel(),
    controller: NavHostController
) {
    val screenState = viewModel.state.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbarController.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            println("Event received.")
            when (event) {
                is SearchScreenEvents.ShowSnackBarMessage -> {
                    launch {
                        snackbar.show(event.message)
                    }
                }
            }
        }
    }

    SearchMovieScreen(
        state = screenState,
        onSearchText = {
            println("text searched: $it")
            viewModel.onActionReceived(SearchAction.OnTextSearched(it))
        }, onTextChange = {
            println("text changed: $it")
            viewModel.onActionReceived(SearchAction.OnTextChanged(it))
        }, onMovieClicked = {
            println("movie clicked: ${it.title}")
            viewModel.onActionReceived(SearchAction.MovieClicked(it))
            controller.navigate(Screens.Detail.route + "/${it.id}")
        }) {
        controller.popBackStack()
    }
}

@Composable
fun SearchMovieScreen(
    state: State<SearchScreenState>,
    onSearchText: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onMovieClicked: (Movie) -> Unit,
    onCancelViewClicked: () -> Unit
) {

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (searchView, list, totalResult, loading) = createRefs()

        if (state.value.isLoading) {
            CircularProgressIndicator(
                Modifier.constrainAs(loading) {
                    top.linkTo(searchView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.value(45.dp)
                    height = Dimension.value(45.dp)
                },
                color = Color.Red,
                strokeWidth = 2.dp
            )
        }

        SearchView(
            Modifier.constrainAs(searchView) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            },
            state,
            onSearchText,
            onTextChange,
            onCancelViewClicked
        )

        if (state.value.searchResult.isNotEmpty()) {
            Text(
                "${state.value.searchResult.size} items found",
                Modifier
                    .constrainAs(totalResult) {
                        top.linkTo(searchView.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
                    .padding(horizontal = 15.dp)
                    .padding(top = 10.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red
            )

            LazyColumn(Modifier.constrainAs(list) {
                top.linkTo(totalResult.bottom, margin = 5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }) {
                itemsIndexed(state.value.searchResult) { index, movie ->
                    SearchResultItem(movie, Modifier.clickable {
                        onMovieClicked(movie)
                    })
                }
            }
        } else if (state.value.searchResultCount == 0) {
            Text(
                "${state.value.searchResult.size} items found !!",
                Modifier
                    .constrainAs(totalResult) {
                        top.linkTo(searchView.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
                    .padding(horizontal = 15.dp)
                    .padding(top = 10.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SearchView(
    modifier: Modifier,
    state: State<SearchScreenState>,
    onSearchText: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onCancelViewClicked: () -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            modifier = Modifier.weight(
                animateFloatAsState(
                    if (isFocused) 0.8f else 1f,
                    label = ""
                ).value
            ),
            searchText = state.value.searchText,
            hintText = "Search Movie",
            textStyle = TextStyle(fontSize = 16.sp),
            onFocusChanged = { focused ->
                isFocused = focused
            },
            onClearButtonClicked = {
                onTextChange("")
            },
            onValueChanged = {
                onTextChange(it)
            },
            onSearchButtonClicked = onSearchText
        )

        AnimatedVisibility(
            isFocused,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            TextButton(
                onClick = onCancelViewClicked, colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text(
                    stringResource(R.string.search_cancel_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}