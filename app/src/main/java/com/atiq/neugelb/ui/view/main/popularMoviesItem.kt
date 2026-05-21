package com.atiq.neugelb.ui.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.atiq.neugelb.data.models.ImageType
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.ui.view.common.PagerIndicator
import com.atiq.neugelb.ui.view.common.UserPercentageView

@Composable
fun popularMoviesItem(
    modifier: Modifier = Modifier,
    screenState: MainScreenState,
    totalMovies: Int,
    pagerPlayTogggleClicked: () -> Unit,
    onItemClicked: (Movie) -> Unit,
    movieByIndex: (Int) -> Movie
): PagerState {
    val pager = rememberPagerState(initialPage = 0, pageCount = {
        totalMovies
    })

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val pageWidth = screenWidthDp * 0.8f
    val gap = 20.dp
    val sidePeek = screenWidthDp - pageWidth
    Box {
        Column(
            modifier
                .wrapContentSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pager,
                pageSize = PageSize.Fixed(pageWidth),
                contentPadding = PaddingValues(horizontal = sidePeek / 2),
                pageSpacing = gap,
                modifier = Modifier.wrapContentSize()
            ) { index ->
                val movie = movieByIndex.invoke(index)

                Card(Modifier.clickable {
                    onItemClicked(movie)
                }) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            model = movie.getImageUrl(ImageType.POSTER),
                            loading = {
                                Box(Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(35.dp)
                                            .align(Alignment.Center), color = Color.Red
                                    )
                                }
                            },
                            error = {
                                Text("Failed to load image.", color = Color.Red)
                            },
                            contentDescription = null,
                        )

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .background(Color.Black.copy(alpha = 0.65f))
                                .align(Alignment.BottomCenter)
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(5.dp)
                                    .align(Alignment.CenterStart),
                                text = movie.title,
                                maxLines = 2,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Column(
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 10.dp, top = 10.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "User Score",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .padding(horizontal = 15.dp, vertical = 5.dp),
                                color = Color.Green,
                                fontWeight = FontWeight.Bold
                            )

                            UserPercentageView(
                                Modifier,
                                progress = movie.getUserPercentage()
                            )
                        }
                    }
                }

            }

            PagerIndicator(
                pager.currentPage,
                pager.pageCount,
                Color.Red.copy(alpha = 0.35f),
                Color.Red,
                Modifier
                    .wrapContentSize()
                    .padding(top = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp)
                .padding(bottom = 35.dp)
        ) {
            FloatingActionButton(
                onClick = pagerPlayTogggleClicked,
                modifier = Modifier
                    .size(35.dp),
                shape = CircleShape,
                containerColor = Color.Red.copy(alpha = 0.85f),
                contentColor = Color.White
            ) {
                Icon(
                    if (screenState.isPagerAutoScrollPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = "Play Pager",
                    Modifier.padding(10.dp)
                )
            }
        }
    }

    return pager
}