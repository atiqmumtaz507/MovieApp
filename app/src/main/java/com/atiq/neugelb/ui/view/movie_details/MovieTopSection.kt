package com.atiq.neugelb.ui.view.movie_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.atiq.neugelb.data.models.ImageType
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.ui.view.common.MoveableBorder

@Composable
fun ConstraintLayoutScope.MovieTopSection(
    movie: Movie,
    bannerImg: ConstrainedLayoutReference,
    smallImg: ConstrainedLayoutReference
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(movie.getbackimg(ImageType.HIGH_QUALITY))
            .crossfade(true)
            .build(),
        contentDescription = movie.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .constrainAs(bannerImg) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            .height(300.dp)
            .blur(1.dp)
    )

    MoveableBorder(
        Modifier
            .constrainAs(smallImg) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(bannerImg.bottom)
                bottom.linkTo(bannerImg.bottom)
                width = Dimension.value(250.dp)
                height = Dimension.value(250.dp)
            },
        shape = RoundedCornerShape(20.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.getImageUrl(ImageType.SMALL_CARDS))
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop
        )
    }
}