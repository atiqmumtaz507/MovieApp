package com.atiq.neugelb.ui.view.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.atiq.neugelb.R
import com.atiq.neugelb.data.models.ImageType
import com.atiq.neugelb.data.models.Movie

@Composable
fun SearchResultItem(movie: Movie, modifier: Modifier) {
    Box(modifier
        .fillMaxWidth()
        .height(220.dp)) {
        val value = ColorUtils.blendARGB(
            Color.Red.toArgb(),
            Color.Black.toArgb(),
            0.25f
        )
        Canvas(Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
            .clipToBounds()) {

            //Background left
            drawRoundRect(
                Color.Black,
                size = Size(size.width / 2, size.height),
                cornerRadius = CornerRadius(18.dp.value),
            )

            //Border
            drawRoundRect(
                Color.Black,
                size = Size(size.width / 2, size.height),
                cornerRadius = CornerRadius(18.dp.value),
                style = Stroke(1f)
            )

            drawRoundRect(
                Color(value),
                topLeft = Offset(x = center.x, y = 0f),
                size = Size(size.width / 2, size.height),
                cornerRadius = CornerRadius(18.dp.value)
            )
            //Border right
            drawRoundRect(
                Color.Black,
                topLeft = Offset(x = center.x, y = 0f),
                size = Size(size.width / 2, size.height),
                cornerRadius = CornerRadius(18.dp.value),
                style = Stroke(1f)
            )

            drawCircle(
                Color.White,
                radius = 25f,
                center = Offset(center.x, -5f)
            )

            drawCircle(
                Color.Black,
                radius = 25f,
                center = Offset(center.x, -5f),
                style = Stroke(1f)
            )

            drawCircle(
                Color.White,
                radius = 25f,
                center = Offset(center.x, size.height)
            )

            drawCircle(
                Color.Black,
                radius = 25f,
                center = Offset(center.x, size.height),
                style = Stroke(1f)
            )
        }

        Box(Modifier
            .padding(start = 2.dp)
            .fillMaxWidth(0.52f)
            .height(220.dp)
            .padding(14.dp)
            .clip(RoundedCornerShape(14.dp))
        ) {
            //Left Container Box
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.getImageUrl(ImageType.POSTER))
                    .crossfade(true)
                    .error(R.drawable.error)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Box(Modifier
            .fillMaxWidth(0.5f)
            .height(300.dp)
            .padding(14.dp)
            .clip(RoundedCornerShape(18.dp))
            .align(Alignment.CenterEnd)
        ) {
            //Right Container Box
            Column(Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
                .padding(end = 10.dp)) {
                Column(Modifier.fillMaxWidth().heightIn(min = 130.dp)) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    VerticalSpacer(4.dp)

                    Text(
                        text = movie.releaseDate?.take(4) ?: "Unknown year",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )

                    VerticalSpacer()

                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 4,
                        color = Color.Yellow,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                RatingView(movie)
            }
        }
    }
}