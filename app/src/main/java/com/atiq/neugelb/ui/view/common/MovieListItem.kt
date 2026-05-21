package com.atiq.neugelb.ui.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.atiq.neugelb.data.models.ImageType
import com.atiq.neugelb.data.models.Movie

@Composable
fun MovieListItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: (Movie) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(movie) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .height(210.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            // Movie Poster image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.getImageUrl(ImageType.POSTER))
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
            )

            // Content
            ContentViewBox(movie)
        }
    }
}

@Composable
private fun ContentViewBox(movie: Movie) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            InfoTextsViews(movie)

            // Rating
            RatingView(movie)

            VerticalSpacer()

            GenreView(movie)
        }
    }
}
@Composable
private fun InfoTextsViews(movie: Movie) {
    Column {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Red,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        VerticalSpacer(4.dp)

        Text(
            text = movie.releaseDate?.take(4) ?: "Unknown year",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        VerticalSpacer()

        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
fun RatingView(movie: Movie) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.Black)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${movie.getUserPercentage().toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }

        HorizontalSpacer()

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.Black)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${movie.voteCount} votes",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}

@Composable
private fun GenreView(movie: Movie) {
    if(movie.genreNames.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            movie.genreNames.take(2).forEach { genreName ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = genreName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    shape = RoundedCornerShape(999.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.Red,
                        labelColor = Color.White
                    )
                )
                HorizontalSpacer()
            }
        }
    }
}