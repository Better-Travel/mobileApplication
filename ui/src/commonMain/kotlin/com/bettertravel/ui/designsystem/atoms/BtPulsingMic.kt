package com.bettertravel.ui.designsystem.atoms

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_mic

private const val PULSE_DURATION_MILLIS = 1600
private const val RING_GROWTH = 0.7f

@Composable
fun BtPulsingMic(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
) {
    val ringColor = BetterTravelTheme.colors.brand
    val transition = rememberInfiniteTransition(label = "mic-pulse")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(PULSE_DURATION_MILLIS, easing = LinearEasing), RepeatMode.Restart),
        label = "mic-ring",
    )
    val buttonScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(PULSE_DURATION_MILLIS / 2, easing = LinearEasing), RepeatMode.Reverse),
        label = "mic-scale",
    )
    Box(
        modifier =
            modifier
                .size(size)
                .drawBehind {
                    if (isListening) {
                        listOf(progress, (progress + 0.5f) % 1f).forEach { ring ->
                            val radius = this.size.minDimension / 2f * (1f + RING_GROWTH * ring)
                            drawCircle(color = ringColor.copy(alpha = 0.3f * (1f - ring)), radius = radius)
                        }
                    }
                },
        contentAlignment = Alignment.Center,
    ) {
        BtIconButton(
            icon = Res.drawable.ic_mic,
            onClick = onClick,
            modifier = Modifier.scale(if (isListening) buttonScale else 1f),
            style = BtIconButtonStyle.Hero,
            size = size,
            iconSize = 28.dp,
        )
    }
}

@Preview
@Composable
private fun BtPulsingMicPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.xxxl)) {
            BtPulsingMic(isListening = true, onClick = {})
            BtPulsingMic(isListening = false, onClick = {})
        }
    }
}
