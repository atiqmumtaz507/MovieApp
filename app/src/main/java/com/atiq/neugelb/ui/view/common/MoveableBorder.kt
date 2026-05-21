package com.atiq.neugelb.ui.view.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MoveableBorder(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(size = 0.dp),
    borderWidth: Dp = 2.dp,
    gradient: Brush = Brush.sweepGradient(
        listOf(Color.Magenta, Color.Cyan, Color.Red, Color.White)
    ),
    animationDuration: Int = 15000,
    content: @Composable () -> Unit
) {
    val infiniteTrx = rememberInfiniteTransition()
    val degrees by infiniteTrx.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration),
            repeatMode = RepeatMode.Restart
        ),
        label = "Infinite colors"
    )

    Surface(shape = shape, modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(borderWidth)
                .drawWithContent {
                    rotate(degrees = degrees) {
                        drawCircle(
                            brush = gradient,
                            radius = size.width,
                            blendMode = BlendMode.SrcIn
                        )
                    }
                    drawContent()
                },
            color = MaterialTheme.colorScheme.surface,
            shape = shape
        ) {
            content()
        }
    }
}