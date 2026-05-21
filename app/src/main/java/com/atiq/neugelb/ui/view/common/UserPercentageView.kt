package com.atiq.neugelb.ui.view.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserPercentageView(
    modifier: Modifier = Modifier,
    progressColor: Color = Color.Green,
    trackColor: Color = Color.LightGray.copy(alpha = 0.25f),
    strokeWidth: Float = 8f,
    progress: Double = 0.0
) {

    val percentageProgress = remember { percentageToSweepAngle(progress) }

    Card(
        modifier
            .size(75.dp)
            .padding(3.dp),
        shape = CircleShape
    ) {
        val gradient = Brush.radialGradient(
            listOf(Color.Black.copy(alpha = 0.65f), Color.Black),
            radius = 300f
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Canvas(
                Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                drawArc(
                    trackColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset.Zero,
                    style = Stroke(strokeWidth)
                )

                drawArc(
                    progressColor,
                    startAngle = -90f,
                    sweepAngle = percentageProgress,
                    useCenter = false,
                    topLeft = Offset.Zero,
                    style = Stroke(strokeWidth)
                )
            }

            PercentText(
                progress.toInt(),
                modifier = Modifier
                    .align(Alignment.Center),
                numberSize = 16.sp,
                percentSize = 12.sp,
                color = progressColor
            )
        }
    }
}

private fun percentageToSweepAngle(percent: Double): Float {
    return (percent.coerceIn(0.0, 100.0) * 360 / 100).toFloat()
}

@Composable
fun PercentText(
    percent: Int,
    modifier: Modifier = Modifier,
    numberSize: TextUnit = 72.sp,
    percentSize: TextUnit = 28.sp,
    color: Color = Color.White
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontSize = numberSize,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            ) {
                append(percent.toString())
            }

            withStyle(
                SpanStyle(
                    fontSize = percentSize,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    baselineShift = BaselineShift(0.98f)
                )
            ) {
                append("%")
            }
        }
    )
}