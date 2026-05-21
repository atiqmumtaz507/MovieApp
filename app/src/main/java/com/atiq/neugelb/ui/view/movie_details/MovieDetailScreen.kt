package com.atiq.neugelb.ui.view.movie_details

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.atiq.neugelb.R
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.ui.view.common.StarRating
import com.atiq.neugelb.ui.view.common.VerticalSpacer
import com.atiq.neugelb.ui.view.main.LocalSnackbarController
import com.atiq.neugelb.viewmodels.MovieDetailViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MovieDetailScreenContainer(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    movieId: Long,
    controller: NavHostController
) {
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = LocalSnackbarController.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            println("Event received.")
            when (event) {
                is DetailScreenEvent.ShowSnackBarMessageWithId -> {
                    val msg = context.getString(event.resourceId)
                    snackbar.show(msg)
                }
            }
        }
    }

    viewModel.attachMovieId(movieId)
    MovieDetailScreen(screenState, onFavouriteButtonClicked = {
        viewModel.onActionReceived(MovieDetailScreenAction.BookMarkClicked)
    }) {
        controller.popBackStack()
    }
}

@Composable
fun MovieDetailScreen(
    state: State<MovieDetailsScreenState>,
    onFavouriteButtonClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    val scrollState = rememberScrollState()
    val movieDetailScreenState = state.value
    movieDetailScreenState.movie?.let { movie ->
        Row(
            Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.DarkGray, Color.Red.copy(alpha = 0.35f), Color.LightGray
                        )
                    )
                )
                .zIndex(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed, Modifier.padding(start = 4.dp)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "back",
                    tint = Color.Red
                )
            }

            IconButton(onClick = onFavouriteButtonClicked, Modifier.padding(end = 4.dp)) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    "bookmark",
                    tint = Color.Red
                )
            }
        }

        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.Black)
        ) {
            val (
                bannerImg, smallImg,
                title, overviewBox, ratingLabel, rating
            ) = createRefs()
            MovieTopSection(
                movie,
                bannerImg,
                smallImg
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(smallImg.bottom, margin = 20.dp)
                        height = Dimension.wrapContent
                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 20.dp),
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Red,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .constrainAs(overviewBox) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(title.bottom, margin = 10.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    },
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                OverViewBox(movie)
            }

            Text(
                modifier = Modifier
                    .constrainAs(ratingLabel) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(overviewBox.bottom, margin = 10.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
                    .padding(horizontal = 20.dp),
                text = stringResource(R.string.user_score),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Red
            )

            StarRating(
                rating = movie.voteAverage,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .constrainAs(rating) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(ratingLabel.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    },
                emptyColor = Color.White
            )
        }
    }
}

@Composable
fun OverViewBox(
    movie: Movie
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation = animateFloatAsState(
        if (isExpanded) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Column(
        Modifier
            .animateContentSize(
                animationSpec = if (isExpanded) {
                    spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                } else {
                    tween(
                        durationMillis = 700,
                        easing = FastOutSlowInEasing
                    )
                }
            )
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis
        )

        VerticalSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
                .clickable {
                    isExpanded = !isExpanded
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (!isExpanded) stringResource(R.string.detail_more_btn) else stringResource(
                    R.string.detail_less_btn
                ),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 10.dp)
            )

            Icon(
                Icons.Default.ArrowDropDown,
                null,
                tint = Color.White,
                modifier = Modifier
                    .alignByBaseline()
                    .graphicsLayer {
                        rotationZ = rotation.value
                    }
            )
        }

    }
}