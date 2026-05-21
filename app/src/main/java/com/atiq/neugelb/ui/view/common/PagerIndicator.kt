package com.atiq.neugelb.ui.view.common

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    currentPage: Int,
    pageCount: Int,
    color: Color,
    selectedColor: Color,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier) {
        repeat(pageCount) {
            val selected = it == currentPage
            IndicatorItem(isSelected = selected, if (selected) selectedColor else color)
            Spacer(Modifier.size(5.dp))
        }
    }
}

@Composable
fun IndicatorItem(
    isSelected: Boolean,
    color: Color
) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 20.dp else 10.dp,
        animationSpec = tween(durationMillis = if (isSelected) 500 else 0, easing = LinearOutSlowInEasing)
    )
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(10.dp)
            .width(width)
            .clip(CircleShape)
            .background(color)
    )
}