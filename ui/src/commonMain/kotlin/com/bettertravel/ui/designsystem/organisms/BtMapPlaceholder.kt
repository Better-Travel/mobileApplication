package com.bettertravel.ui.designsystem.organisms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtMarkerStyle
import com.bettertravel.ui.designsystem.atoms.BtNumberMarker
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtGradients

@Composable
fun BtMapPlaceholder(
    modifier: Modifier = Modifier,
    overlay: @Composable () -> Unit = {},
) {
    val colors = BetterTravelTheme.colors
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(colors.surfaceContainer),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val grid = 48.dp.toPx()
            var x = 0f
            while (x < size.width) {
                drawLine(colors.surfaceContainerHighest, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                x += grid
            }
            var y = 0f
            while (y < size.height) {
                drawLine(colors.surfaceContainerHighest, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += grid
            }
            val route =
                Path().apply {
                    moveTo(size.width * 0.2f, size.height * 0.75f)
                    cubicTo(
                        size.width * 0.25f,
                        size.height * 0.3f,
                        size.width * 0.7f,
                        size.height * 0.8f,
                        size.width * 0.8f,
                        size.height * 0.25f,
                    )
                }
            drawPath(
                path = route,
                brush = BtGradients.mapRing,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round),
            )
        }
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(start = 40.dp, bottom = 56.dp)) {
            BtNumberMarker(1, style = BtMarkerStyle.Primary, size = 32.dp)
        }
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 56.dp, top = 56.dp)) {
            BtNumberMarker(2, style = BtMarkerStyle.Coral, size = 36.dp)
        }
        overlay()
    }
}

@Preview
@Composable
private fun BtMapPlaceholderPreview() {
    BtPreview {
        BtMapPlaceholder(modifier = Modifier.height(320.dp))
    }
}
