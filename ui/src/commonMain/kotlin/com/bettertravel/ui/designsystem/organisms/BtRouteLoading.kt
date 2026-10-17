package com.bettertravel.ui.designsystem.organisms

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty_route_loading
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BtRouteLoading(
    message: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    val palette = listOf(colors.brand, colors.coral, colors.gold, colors.primary, colors.coralSoft)
    val transition = rememberInfiniteTransition(label = "route-loading")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(CHUNK_CYCLE_MILLIS, easing = LinearEasing)),
        label = "chunks",
    )
    val bob by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(BOB_MILLIS, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "bob",
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BtSpacing.l),
    ) {
        Box(modifier = Modifier.size(STAGE_SIZE), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(STAGE_SIZE)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val reach = size.minDimension / 2f
                for (index in 0 until CHUNK_COUNT) {
                    val phase = index.toFloat() / CHUNK_COUNT
                    val t = (progress + phase) % 1f
                    val angle = index * GOLDEN_ANGLE
                    val distance = reach * (0.35f + 0.65f * ((index * 7) % CHUNK_COUNT) / CHUNK_COUNT) * easeOut(t)
                    val x = center.x + cos(angle) * distance
                    val y = center.y + sin(angle) * distance - t * CHUNK_RISE.toPx()
                    val fadeIn = (t * 6f).coerceAtMost(1f)
                    val chunkAlpha = (1f - t) * fadeIn
                    val width = (CHUNK_MIN + (index % 3) * CHUNK_STEP).dp.toPx()
                    val chunkSize = Size(width, width * 0.6f)
                    rotate(degrees = (angle * 180f / PI.toFloat()) + t * 360f, pivot = Offset(x, y)) {
                        drawRoundRect(
                            color = palette[index % palette.size],
                            topLeft = Offset(x - chunkSize.width / 2f, y - chunkSize.height / 2f),
                            size = chunkSize,
                            cornerRadius = CornerRadius(chunkSize.height / 3f),
                            alpha = chunkAlpha,
                        )
                    }
                }
            }
            Image(
                painter = painterResource(Res.drawable.betty_route_loading),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .size(IMAGE_SIZE)
                        .graphicsLayer { translationY = bob * BOB_DISTANCE.toPx() },
            )
        }
        Text(
            text = message,
            style = BetterTravelTheme.typography.headline,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = BtSpacing.xxl).alpha(1f),
        )
    }
}

private fun easeOut(t: Float): Float {
    return 1f - (1f - t) * (1f - t)
}

private val STAGE_SIZE = 320.dp
private val IMAGE_SIZE = 220.dp
private val BOB_DISTANCE = 8.dp
private val CHUNK_RISE = 24.dp
private const val CHUNK_COUNT = 20
private const val CHUNK_MIN = 7
private const val CHUNK_STEP = 3
private const val GOLDEN_ANGLE = 2.3999632f
private const val CHUNK_CYCLE_MILLIS = 2600
private const val BOB_MILLIS = 1800

@Preview
@Composable
private fun BtRouteLoadingPreview() {
    BtPreview {
        BtRouteLoading(message = "Betty is preparing a route for you")
    }
}
