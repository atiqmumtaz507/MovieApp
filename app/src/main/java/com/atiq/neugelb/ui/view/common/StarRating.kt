package com.atiq.neugelb.ui.view.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    starCount: Int = 5,
    starSize: Dp = 22.dp,
    spacing: Dp = 4.dp,
    filledColor: Color = Color(0xFFFFC107),
    emptyColor: Color = Color.Gray.copy(alpha = 0.20f),
) {
    val clamped = rating.coerceIn(0.0, 10.0)
    val stars = clamped / 10f * starCount

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            repeat(starCount) { index ->
                val fillFraction = (stars - index).coerceIn(0.0, 1.0)

                StarItem(
                    fillFraction = fillFraction,
                    size = starSize,
                    filledColor = filledColor,
                    emptyColor = emptyColor
                )

                if (index != starCount - 1)
                    HorizontalSpacer(spacing)
            }
        }
    }
}

@Composable
private fun StarItem(
    fillFraction: Double,
    size: Dp,
    filledColor: Color,
    emptyColor: Color
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Always-visible empty star
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = emptyColor,
            modifier = Modifier.fillMaxSize()
        )

        // Filled star clipped by fraction
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fillFraction.toFloat())
                .clipToBounds()
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = filledColor,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
